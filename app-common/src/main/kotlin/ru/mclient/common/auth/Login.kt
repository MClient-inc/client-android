package ru.mclient.common.auth

data class LoginState(
    val username: String,
    val password: String,
    val isLoading: Boolean,
    val isError: Boolean,
)


interface Login {

    val state: LoginState

    fun onUpdate(username: String, password: String)

    fun onLogin(username: String, password: String)

}