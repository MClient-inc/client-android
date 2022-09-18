package ru.mclient.ui

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginUIState(
    val username: String,
    val password: String,
    val isLoading: Boolean,
    val error: String?,
) : Parcelable

class LoginUIInput(
    val username: String,
    val password: String,
)

fun LoginUIState.toInput(
    username: String = this.username,
    password: String = this.password
): LoginUIInput {
    return LoginUIInput(username, password)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    state: LoginUIState,
    onUpdate: (LoginUIInput) -> Unit,
    onLogin: (LoginUIInput) -> Unit,
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
        OutlinedTextField(
            value = state.username,
            onValueChange = { onUpdate(state.toInput(username = it)) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onUpdate(state.toInput(password = it)) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { onLogin(state.toInput()) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
    }
}