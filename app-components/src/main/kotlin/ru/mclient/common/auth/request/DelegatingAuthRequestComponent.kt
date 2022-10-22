package ru.mclient.common.auth.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import ru.mclient.common.auth.AuthRequest
import ru.mclient.common.auth.AuthState

class DelegatingAuthRequestComponent(
    private val onLogin: () -> Unit,
    private val onRegister: () -> Unit,
    state: AuthState,
) : AuthRequest {

    override val state: AuthState by mutableStateOf(state)

    override fun onLogin() {
        onLogin.invoke()
    }

    override fun onRegister() {
        onRegister.invoke()
    }
}