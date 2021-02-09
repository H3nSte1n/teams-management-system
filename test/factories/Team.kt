package factories

import data.Team
import org.joda.time.DateTime

object Team {
    val instance by lazy {
        Team(0, "foo", listOf(1, 2, 3))
    }
}
