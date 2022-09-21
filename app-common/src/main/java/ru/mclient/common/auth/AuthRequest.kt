package ru.mclient.common.auth

import kotlinx.coroutines.flow.StateFlow


class AuthState(
    val isLoginAvailable: Boolean,
    val isRegisterAvailable: Boolean,
)

interface AuthRequest {

    val state: StateFlow<AuthState>

    fun onLogin()

    fun onRegister()

}