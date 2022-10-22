package ru.mclient.common.auth


class AuthState(
    val isLoginAvailable: Boolean,
    val isRegisterAvailable: Boolean,
)

interface AuthRequest {

    val state: AuthState

    fun onLogin()

    fun onRegister()

}