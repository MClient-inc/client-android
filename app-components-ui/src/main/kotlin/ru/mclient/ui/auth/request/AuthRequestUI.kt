package ru.mclient.ui.auth.request

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.auth.AuthRequest
import ru.mclient.common.auth.AuthState
import ru.mclient.ui.agreement.AgreementModalUI
import ru.mclient.ui.auth.Auth
import ru.mclient.ui.auth.AuthUIState

fun AuthState.toUI(): AuthUIState {
    return AuthUIState(
        isLoginAvailable = isLoginAvailable,
        isRegisterAvailable = isRegisterAvailable
    )
}

@Composable
fun AuthRequestUI(component: AuthRequest, modifier: Modifier) {
    AgreementModalUI(component = component.agreement, modifier = modifier) {
        Auth(
            state = component.state.toUI(),
            onLogin = component::onLogin,
            onRegister = component::onRegister,
            onAgreement = component::onAgreement,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}