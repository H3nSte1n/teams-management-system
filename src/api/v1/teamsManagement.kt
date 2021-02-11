
package api.v1

import controller.TeamsController
import data.Team
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import withRole

fun Route.TeamenManagement() {
    withRole("admin") {
        delete("teams/{id}") {
            val teamId = call.parameters["id"]
            val removedTeam = TeamsController.removeTeam(teamId!!.toInt())
            call.respond(removedTeam)
        }
        post("teams") {
            val receivedTeam = call.receive<Team>()
            val addedTeam = TeamsController.addTeam(receivedTeam)
            call.respond(addedTeam)
            call.request.header("Authorization")
        }
        put("teams/{id}") {
            val teamId = call.parameters["id"]
            val newTeamsValues = call.receive<Team>()
            val updatedTeam = TeamsController.updateTeam(teamId!!.toInt(), newTeamsValues)
            call.respond(updatedTeam)
        }
    }
    withRole("admin", "user") {
        get("teams") {
            val storedTeams = TeamsController.getAllTeams()
            call.respond(storedTeams)
        }
        get("teams/{id}") {
            val teamId = call.parameters["id"]
            val storedTeams = TeamsController.getTeam(teamId!!.toInt())
            call.respond(storedTeams)
        }
    }
}
