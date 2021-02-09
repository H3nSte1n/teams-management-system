package helper

import validation.TeamValidation

object Controller {
    fun isInputValid(methods: List<String>): Boolean {
        for (input in methods) {
            if (!TeamValidation.validateInput(input)) return false
        }

        return true
    }
}
