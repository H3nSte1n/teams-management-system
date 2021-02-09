package statuspages

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InvalidTeamStatusPageTest {
    @Nested
    inner class when_throw_exception {

        @Test
        fun response_status_and_message() {
            withTestApplication {
                application.install(StatusPages) {
                    invalidTeamStatusPage()
                }
                application.routing {
                    get("/exception") {
                        throw InvalidTeamException()
                    }
                }

                handleRequest(HttpMethod.Get, "/exception").let { call ->
                    assertEquals(
                        HttpStatusCode.BadRequest,
                        call.response.status(),
                        "Should return status code 400"
                    )
                    assertEquals(
                        InvalidTeamException().message,
                        call.response.content,
                        "Should return error message from InvalidTeamException"
                    )
                }
            }
        }
    }
}
