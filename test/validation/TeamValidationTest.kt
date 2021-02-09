package validation

import DatabaseConnection
import factories.Team
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import schemas.Teams
import kotlin.test.assertEquals

class TeamValidationTest {

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Nested
    inner class validateTeamExist : DatabaseConnection() {

        @Test
        fun should_run_teamExistByName_when_call_method_with_lastname() {
            val Team = Team.instance
            mockkObject(Teams)

            every { Teams.teamExistByName(any()) } returns true

            TeamValidation.validateTeamExist("name", Team.name)

            verify {
                Teams.teamExistByName(any())
            }
        }

        @Test
        fun should_run_TeamExistById_when_call_method_with_id() {
            val Team = Team.instance
            mockkObject(Teams)

            every { Teams.teamExistById(any()) } returns true

            TeamValidation.validateTeamExist("id", Team.id)

            verify {
                Teams.teamExistById(any())
            }
        }

        @Test
        fun should_return_false_when_call_method_with_invalid_attribute() {
            val Team = Team.instance
            mockkObject(Teams)

            every { Teams.teamExistByName(any()) } returns true
            every { Teams.teamExistById(any()) } returns true

            TeamValidation.validateTeamExist("foo", Team.name)

            verify(exactly = 0) {
                Teams.teamExistById(any())
                Teams.teamExistByName(any())
            }
        }

        @Test
        fun should_return_true_if_Team_exist() {
            val Team = Team.instance

            val storedTeam = transaction {
                Teams.insert {
                    it[name] = Team.name
                    it[personList] = Team.personList!!
                }
            }.let {
                data.Team(it[Teams.id], it[Teams.name], it[Teams.personList])
            }

            val returnValue = TeamValidation.validateTeamExist<String>("name", storedTeam.name)
            assertEquals(true, returnValue)
        }

        @Test
        fun should_return_false_if_Team_not_exist() {
            val returnValue = TeamValidation.validateTeamExist<String>("name", "foobar")
            assertEquals(false, returnValue)
        }
    }

    @Nested
    inner class validateInput {

        @Test
        fun should_return_false_if_string_is_blank() {
            val returnValue = TeamValidation.validateInput(" ")
            assertEquals(false, returnValue)
        }

        @Test
        fun should_return_false_if_string_is_empty() {
            val returnValue = TeamValidation.validateInput("")
            assertEquals(false, returnValue)
        }

        @Test
        fun should_return_true_if_string_is_not_empty_or_blank() {
            val returnValue = TeamValidation.validateInput("foobar")
            assertEquals(true, returnValue)
        }
    }
}
