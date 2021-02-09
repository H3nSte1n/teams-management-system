package controller

import ClassHelper
import data.Team
import helper.Controller.isInputValid
import schemas.Teams
import schemas.Teams.createTeam
import schemas.Teams.getTeams
import statuspages.InvalidTeamException
import statuspages.ThrowableException
import validation.TeamValidation.validateTeamExist

object TeamsController {
    fun removeTeam(id: Int): Int {
        if (!validateTeamExist("id", id)) throw InvalidTeamException()

        return Teams.deleteTeam(id)
    }

    fun addTeam(receivedTeam: Team): Team {
        val inputs = listOf(receivedTeam.name)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (validateTeamExist("String", receivedTeam.name)) throw InvalidTeamException()

        return createTeam(receivedTeam)
    }

    fun getAllTeams(): Collection<Team> {
        return getTeams()
    }

    fun getTeam(TeamId: Int): Team {
        return Teams.getTeam(TeamId)
    }

    fun updateTeam(id: Int, newTeamValues: Team): Team {
        var inputs: MutableList<String> = ClassHelper.getAllPropertyValues(newTeamValues)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (!validateTeamExist("id", id)) throw InvalidTeamException()

        return Teams.updateTeam(id, newTeamValues)
    }
}
