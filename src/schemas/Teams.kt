package schemas

import arrayOfLong
import data.Team
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Teams : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 60)
    val personList = arrayOfLong("personList")

    fun getTeam(customerUUID: Int): Team {
        return transaction {
            select { id eq customerUUID }.first()
        }.let { it ->
            Team(it[id], it[name], it[personList])
        }
    }

    fun getTeams(): Collection<Team> {
        return transaction {
            selectAll().map {
                Team(it[id], it[name], it[personList])
            }
        }
    }

    fun teamExistById(customerId: Int): Boolean {
        return transaction {
            select { id eq customerId }.empty().not()
        }
    }

    fun teamExistByName(customerName: String): Boolean {
        return transaction {
            select { name eq customerName }.empty().not()
        }
    }

    fun deleteTeam(givenUUID: Int): Int {
        return transaction {
            deleteWhere { id eq givenUUID }
        }
    }

    fun updateTeam(selectedId: Int, team: Team): Team {
        return transaction {
            update({ id eq selectedId }) {
                it[name] = team.name
                it[personList] = team.personList!!
            }
        }.let {
            getTeam(selectedId)
        }
    }

    fun createTeam(team: Team): Team {
        return transaction {
            insert {
                it[name] = team.name
                it[personList] = team.personList!!
            }
        }.let {
            Team(it[id], it[name], it[personList])
        }
    }
}
