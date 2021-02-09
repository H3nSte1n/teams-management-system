package schemas

import DatabaseConnection
import DatabaseUtil.assertColumnExist
import DatabaseUtil.assertColumnType
import data.Team
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class TeamsTest : DatabaseConnection() {

    @BeforeTest
    fun prepare() {
        prepareTables()
    }

    @Nested
    inner class integration_tests {
        @Test
        @DisplayName("All Columns are avaiable")
        fun testColumnsExist() {
            transaction {
                assertColumnExist("id", Teams)
                assertColumnExist("name", Teams)
                assertColumnExist("personList", Teams)
            }
        }

        @Test
        @DisplayName("All Columns has the correct sql-type")
        fun testColumnsType() {
            transaction {
                assertColumnType("id", Teams, "serial")
                assertColumnType("name", Teams, "varchar(60)")
                assertColumnType("personList", Teams, "bigint[]")
            }
        }
    }

    @Nested
    inner class getTeam {

        @Test
        fun should_find_and_return_Team_by_name() {
            val persistedTeam = createAndStoreTeam()

            val selectedTeam = Teams.getTeam(persistedTeam.id!!)
            assertNotNull(selectedTeam.id)
            assertEquals(persistedTeam.name, selectedTeam.name)
            assertEquals(persistedTeam.personList, selectedTeam.personList)
        }
    }

    @Nested
    inner class getTeams {

        @Test
        fun should_find_and_return_Teams_Collection() {
            val persistedTeam = createAndStoreTeam()

            val teamCollection = Teams.getTeams()
            assertNotNull(teamCollection.first().id)
            assertEquals(persistedTeam.name, teamCollection.first().name)
            assertEquals(persistedTeam.personList, teamCollection.first().personList)
        }
    }

    @Nested
    inner class TeamExistById {

        @Test
        fun should_return_true_if_Team_exists() {
            val persistedTeam = createAndStoreTeam()

            val teamExist = Teams.teamExistById(persistedTeam.id!!)
            assertTrue(teamExist)
        }

        @Test
        fun should_return_false_if_Team_exists() {
            val persistedTeam = createAndStoreTeam()

            val teamExist = Teams.teamExistById(persistedTeam.id!! + 1)
            assertFalse(teamExist)
        }
    }

    @Nested
    inner class teamExistByName {
        lateinit var persistedTeam: Team

        @BeforeEach
        fun prepare() {
            persistedTeam = createAndStoreTeam()
        }

        @Test
        fun should_return_true_if_Team_exists() {
            val TeamExist = Teams.teamExistByName(persistedTeam.name)
            assertTrue(TeamExist)
        }

        @Test
        fun should_return_false_if_Team_exists() {
            val TeamExist = Teams.teamExistByName(persistedTeam.name)
            assertTrue(TeamExist)
        }
    }

    @Nested
    inner class deleteTeam {
        lateinit var persistedTeam: Team

        @BeforeEach
        fun prepare() {
            persistedTeam = createAndStoreTeam()
        }

        @Test
        fun should_remove_Team_if_exists() {
            Teams.deleteTeam(persistedTeam.id!!)

            val TeamExist = transaction {
                Teams.select { Teams.id eq persistedTeam.id!! }.empty().not()
            }
            assertFalse(TeamExist)
        }

        @Test
        fun should_ignore_call_if_Team_not_exists() {
            assertDoesNotThrow { Teams.deleteTeam(persistedTeam.id!! + 1) }
        }
    }

    @Nested
    inner class updateTeam {
        lateinit var persistedTeam: Team

        @BeforeEach
        fun prepare() {
            persistedTeam = createAndStoreTeam()
        }

        @Test
        fun should_update_Team_if_exists() {
            val newTeamData = Team(
                null,
                "hallo",
                listOf(1, 2, 3),
            )
            val updatedTeam = Teams.updateTeam(persistedTeam.id!!, newTeamData)

            assertNotNull(persistedTeam.id)
            assertEquals(updatedTeam.name, newTeamData.name)
            assertEquals(updatedTeam.personList, newTeamData.personList)
        }
    }

    @Nested
    inner class createTeam {

        @Test
        fun should_create_and_persist_a_new_Team() {
            val Team = factories.Team.instance
            Teams.createTeam(Team)
            val persistedTeam = transaction {
                Teams.select { Teams.name eq Team.name }.first()
            }.let {
                Team(it[Teams.id], it[Teams.name], it[Teams.personList])
            }

            assertNotNull(persistedTeam.id)
            assertEquals(Team.name, persistedTeam.name)
            assertEquals(Team.personList, persistedTeam.personList)
        }
    }

    private fun createAndStoreTeam(): Team {
        val Team = factories.Team.instance

        return transaction {
            Teams.insert {
                it[name] = Team.name
                it[personList] = Team.personList!!
            }
        }.let {
            Team(it[Teams.id], it[Teams.name], it[Teams.personList])
        }
    }

    private fun prepareTables() {
        transaction {
            exec("TRUNCATE Teams RESTART IDENTITY CASCADE;")
        }
    }
}
