package controller

import factories.Team
import helper.Controller
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import schemas.Teams
import statuspages.InvalidTeamException
import statuspages.ThrowableException
import validation.TeamValidation
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TeamsControllerTest {
    lateinit var team: data.Team

    @BeforeTest
    fun prepare() {
        team = Team.instance
        unmockkAll()
    }

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Nested
    inner class when_run_removeTeam {

        @Test
        fun should_call_specific_methods() {
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { TeamValidation.validateTeamExist<Int>(any(), any()) } returns true
            every { Teams.deleteTeam(any()) } returns 1

            TeamsController.removeTeam(3)

            verify {
                TeamValidation.validateTeamExist<Int>(any(), any())
                Teams.deleteTeam(any())
            }

            verifyOrder {
                TeamValidation.validateTeamExist<Int>(any(), any())
                Teams.deleteTeam(any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(TeamValidation)

            every { TeamValidation.validateTeamExist<Int>(any(), any()) } returns false

            assertThrows(InvalidTeamException::class.java) {
                TeamsController.removeTeam(3)
            }
        }

        @Test
        fun should_return_count_of_removed_Teams_on_valid_request() {
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { TeamValidation.validateTeamExist<Int>(any(), any()) } returns true
            every { Teams.deleteTeam(any()) } returns 1

            assertEquals(TeamsController.removeTeam(3), 1)
        }
    }

    @Nested
    inner class when_run_addTeam {

        @Test
        fun should_call_specific_methods() {
            mockkObject(Controller)
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns false
            every { Teams.createTeam(any()) } returns team

            TeamsController.addTeam(team)

            verify {
                Controller.isInputValid(any())
                TeamValidation.validateTeamExist<String>(any(), any())
                Teams.createTeam(any())
            }

            verifyOrder {
                Controller.isInputValid(any())
                TeamValidation.validateTeamExist<String>(any(), any())
                Teams.createTeam(any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(Controller)

            every { Controller.isInputValid(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                TeamsController.addTeam(team)
            }
        }

        @Test
        fun should_break_up_if_Team_already_exist() {
            mockkObject(Controller)
            mockkObject(TeamValidation)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns true

            assertThrows(InvalidTeamException::class.java) {
                TeamsController.addTeam(team)
            }
        }

        @Test
        fun should_return_added_Team_on_valid_request() {
            mockkObject(Controller)
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns false
            every { Teams.createTeam(any()) } returns team

            assertEquals(TeamsController.addTeam(team), team)
        }
    }

    @Nested
    inner class when_run_getAllTeams {

        @Test
        fun should_call_specific_method() {
            mockkObject(Teams)

            every { Teams.getTeams() } returns listOf(team)

            TeamsController.getAllTeams()

            verify {
                Teams.getTeams()
            }
        }

        @Test
        fun should_return_list_of_stored_Teams_on_valid_request() {
            mockkObject(Teams)

            every { Teams.getTeams() } returns listOf(team)

            assertEquals(TeamsController.getAllTeams(), listOf(team))
        }
    }

    @Nested
    inner class when_run_updateTeam {

        @Test
        fun should_call_specific_method() {
            mockkObject(Controller)
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns true
            every { Teams.updateTeam(any(), any()) } returns team

            TeamsController.updateTeam(1, team)

            verify {
                Controller.isInputValid(any())
                TeamValidation.validateTeamExist<String>(any(), any())
                Teams.updateTeam(any(), any())
            }

            verifyOrder {
                Controller.isInputValid(any())
                TeamValidation.validateTeamExist<String>(any(), any())
                Teams.updateTeam(any(), any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(Controller)

            every { Controller.isInputValid(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                TeamsController.updateTeam(1, team)
            }
        }

        @Test
        fun should_break_up_if_Team_not_exist() {
            mockkObject(Controller)
            mockkObject(TeamValidation)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns false

            assertThrows(InvalidTeamException::class.java) {
                TeamsController.updateTeam(1, team)
            }
        }

        @Test
        fun should_return_updated_Team_on_valid_request() {
            mockkObject(Controller)
            mockkObject(TeamValidation)
            mockkObject(Teams)

            every { Controller.isInputValid(any()) } returns true
            every { TeamValidation.validateTeamExist<String>(any(), any()) } returns true
            every { Teams.updateTeam(any(), any()) } returns team

            assertEquals(TeamsController.updateTeam(1, team), team)
        }
    }
}
