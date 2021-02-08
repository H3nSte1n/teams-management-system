package api.v1

import io.ktor.features.CallId.Feature.phase
import io.ktor.http.*
import io.ktor.routing.*
import statuspages.AuthenticationException

fun Route.v1() {
    route("/v1") {
        apiDoc()
        personenManagement()
    }
}


