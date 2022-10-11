package ru.mclient.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.toDesignedString


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
        DesignedButton(
            text = "Уже есть аккаунт".toDesignedString(),
            onClick = onLogin,
            enabled = state.isLoginAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedButton(
            text = "Я новичок".toDesignedString(),
            onClick = onRegister,
            enabled = state.isRegisterAvailable,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}