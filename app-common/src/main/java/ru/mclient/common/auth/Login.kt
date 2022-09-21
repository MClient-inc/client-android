package ru.mclient.common.auth

import kotlinx.coroutines.flow.StateFlow

data class LoginState(
    val username: String,
    val password: String,
    val isLoading: Boolean,
    val isError: Boolean,
)

interface Login {

    val state: StateFlow<LoginState>

    fun onUpdate(username: String, password: String)

    fun onLogin(username: String, password: String)

}