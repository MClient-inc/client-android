package ru.mclient.ui.auth.request

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.auth.AuthRequest
import ru.mclient.common.auth.AuthState
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
    val state = component.state.collectAsState().value.toUI()

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        Auth(
            state = state,
            onLogin = component::onLogin,
            onRegister = component::onRegister,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        )
    }
}