package statuspages
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.invalidTeamStatusPage() {
    exception<InvalidTeamException> { cause ->
        call.respondText(
            cause.message,
            ContentType.Text.Plain,
            status = HttpStatusCode.BadRequest
        )
    }
}

data class InvalidTeamException(override val message: String = "Invalid Team Exception") : Exception()
