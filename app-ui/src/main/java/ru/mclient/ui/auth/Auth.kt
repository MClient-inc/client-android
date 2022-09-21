package ru.mclient.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.ui.view.DesignedTextButton


data class AuthUIState(
    val isLoginAvailable: Boolean,
    val isRegisterAvailable: Boolean,
)

@Composable
fun Auth(
    state: AuthUIState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        DesignedTextButton(
            text = "Уже есть аккаунт",
            onClick = onLogin,
            enabled = state.isLoginAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedTextButton(
            text = "Я новичок",
            onClick = onRegister,
            enabled = state.isRegisterAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}