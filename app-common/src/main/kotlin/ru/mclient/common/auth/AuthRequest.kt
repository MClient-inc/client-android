package ru.mclient.common.auth

import ru.mclient.common.agreement.AgreementModal


class AuthState(
    val isLoginAvailable: Boolean,
    val isRegisterAvailable: Boolean,
)

interface AuthRequest {

    val state: AuthState

    fun onLogin()

    fun onRegister()

    val agreement: AgreementModal
    fun onAgreement()

}