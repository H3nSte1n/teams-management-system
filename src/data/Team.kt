package data

import com.fasterxml.jackson.annotation.JsonFormat
import org.joda.time.DateTime

data class Team(
    val id: Int?,
    val name: String,
    val personList: List<Int>?,

)
