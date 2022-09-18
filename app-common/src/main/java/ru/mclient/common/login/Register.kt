package ru.mclient.common.login

import kotlinx.coroutines.flow.StateFlow

data class RegisterState(
    val email: String,
    val isEmailValid: Boolean,
    val username: String,
    val password: String,
    val repeatedPassword: String,
    val isError: Boolean,
    val isRegistering: Boolean
)

interface Register {

    val state: StateFlow<RegisterState>

    fun onUpdate(email: String, username: String, password: String, repeatedPassword: String)

    fun onRegister(email: String, username: String, password: String, repeatedPassword: String)

}