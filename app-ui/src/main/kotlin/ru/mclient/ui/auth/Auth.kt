package ru.mclient.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R


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
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painterResource(id = R.drawable.icon),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(250.dp)
                    .fillMaxSize()
            )
        }
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