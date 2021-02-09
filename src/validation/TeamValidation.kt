package validation

import schemas.Teams

object TeamValidation {

    fun <T> validateTeamExist(attribute: String, attributeValue: T): Boolean {
        when (attribute) {
            "id" -> if (Teams.teamExistById(attributeValue as Int)) return true
            "name" -> if (Teams.teamExistByName(attributeValue.toString())) return true
        }

        return false
    }

    fun validateInput(input: String): Boolean {
        if (input.isEmpty()) return false
        if (input.isBlank()) return false

        return true
    }
}
