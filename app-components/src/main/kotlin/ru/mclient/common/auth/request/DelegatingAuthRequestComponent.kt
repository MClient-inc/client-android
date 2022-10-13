package ru.mclient.common.auth.request

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mclient.common.auth.AuthRequest
import ru.mclient.common.auth.AuthState

class DelegatingAuthRequestComponent(
    private val onLogin: () -> Unit,
    private val onRegister: () -> Unit,
    state: AuthState,
) : AuthRequest {

    override val state: StateFlow<AuthState> = MutableStateFlow(state)

    override fun onLogin() {
        onLogin.invoke()
    }

    override fun onRegister() {
        onRegister.invoke()
    }
}