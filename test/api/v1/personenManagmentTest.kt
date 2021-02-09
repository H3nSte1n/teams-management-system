/*import api.api
import com.fasterxml.jackson.databind.SerializationFeature
import controller.TeamController
import data.Team
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TeamenManagmentTest {
    val body =
        """{"firstname":"Henry","lastname":"foobar","date":"2021-02-05 16:34:23.23"}"""
    lateinit var Team: data.Team

    @BeforeTest
    fun prepare() {
        Team = factories.Team.instance
        unmockkAll()
    }

    @Nested
    inner class api_v1_Teams {
        val path = "/api/v1/Teams"

        @BeforeTest
        fun prepare() {
            mockkObject(TeamController)
            every { TeamController.addTeam(any()) } returns Team
        }

        @Test
        fun should_call_TeamController_addTeam() {
            fun tests(call: TestApplicationCall) {
                verify {
                    TeamController.addTeam(any())
                }
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }

        @Test
        fun should_respond_Team() {
            fun tests(call: TestApplicationCall) {
                assertEquals(Team, call.response.content)
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }

        @Test
        fun should_return_200_http_status_code() {
            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.OK, call.response.status())
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }
    }

    @Nested
    inner class api_v1_sign_up {
        val path = "/api/v1/Teams/{id}"

        @BeforeTest
        fun prepare() {
            mockkObject(TeamController)
            every { TeamController.removeTeam(any()) } returns 1
        }

        @Test
        fun should_call_RegisterController_removeTeam() {
            fun tests(call: TestApplicationCall) {
                verify {
                    TeamController.removeTeam(any())
                }
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }

        @Test
        fun should_respond_count_of_deleted_Teams() {
            fun tests(call: TestApplicationCall) {
                assertEquals(1, call.response.content)
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }

        @Test
        fun should_return_200_http_status_code() {
            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.OK, call.response.status())
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }
    }

    private fun initApplication(tests: (call: TestApplicationCall) -> Unit, path: String, body: String, httpVerb: HttpMethod) {
        withTestApplication {
            application.routing {
                api()
            }

            application.install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            handleRequest(httpVerb, path) {
                addHeader("Accept", "*")
                addHeader("Content-Type", "application/json; charset=UTF-16")
                setBody(body.toByteArray(charset = Charsets.UTF_16))
            }.let { call ->
                tests(call)
            }
        }
    }
}
*/
