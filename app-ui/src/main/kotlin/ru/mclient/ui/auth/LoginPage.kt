package ru.mclient.ui.auth

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.parcelize.Parcelize
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString

@Parcelize
data class LoginPageState(
    val username: String,
    val password: String,
    val isLoading: Boolean,
    val error: String?,
) : Parcelable

class LoginPageInput(
    val username: String,
    val password: String,
)

fun LoginPageState.toInput(
    username: String = this.username,
    password: String = this.password
): LoginPageInput {
    return LoginPageInput(username, password)
}

@Composable
fun LoginPage(
    state: LoginPageState,
    onUpdate: (LoginPageInput) -> Unit,
    onLogin: (LoginPageInput) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (!state.error.isNullOrEmpty())
            Text(
                text = state.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
            )
        DesignedTextField(
            value = state.username,
            onValueChange = { onUpdate(state.toInput(username = it)) },
            label = "Имя пользователя",
            enabled = !state.isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedTextField(
            value = state.password,
            onValueChange = { onUpdate(state.toInput(password = it)) },
            label = "Пароль",
            enabled = !state.isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedButton(
            text = "Войти".toDesignedString(),
            onClick = { onLogin(state.toInput()) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}