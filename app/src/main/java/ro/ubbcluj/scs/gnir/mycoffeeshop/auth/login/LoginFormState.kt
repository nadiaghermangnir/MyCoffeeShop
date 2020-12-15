package ro.ubbcluj.scs.gnir.mycoffeeshop.auth.login

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
