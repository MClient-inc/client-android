package ru.mclient.common.login

import kotlinx.coroutines.flow.StateFlow

data class RegisterState(
    val mail: String,
    val username: String,
    val password: String,
    val repeatedPassword: String,
    val isError: Boolean,
    val isRegistering: Boolean
)

interface Register {

    val state: StateFlow<RegisterState>

    fun onUpdate(mail: String, username: String, password: String, repeatedPassword: String)

    fun onRegister(mail: String, username: String, password: String, repeatedPassword: String)

}