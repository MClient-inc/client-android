package ru.mclient.ui

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.parcelize.Parcelize
import ru.shafran.ui.R

@Parcelize
data class RegisterUIState(
    val mail: String,
    val username: String,
    val password: String,
    val repeatedPassword: String,
    val isRegistering: Boolean,
    val error: String?,
) : Parcelable

class RegisterUIInput(
    val mail: String,
    val username: String,
    val password: String,
    val repeatedPassword: String
)

fun RegisterUIState.toInput(
    mail: String = this.mail,
    username: String = this.username,
    password: String = this.password,
    repeatedPassword: String = this.repeatedPassword
): RegisterUIInput {
    return RegisterUIInput(mail, username, password, repeatedPassword)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    state: RegisterUIState,
    onUpdate: (RegisterUIInput) -> Unit,
    onRegister: (RegisterUIInput) -> Unit,
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
            label = {
                Text(
                    text = stringResource(id = R.string.mail)
                )
            },
            value = state.mail,
            onValueChange = { onUpdate(state.toInput(mail = it)) },
            enabled = !state.isRegistering,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.username)
                )
            },
            value = state.username,
            onValueChange = { onUpdate(state.toInput(username = it)) },
            enabled = !state.isRegistering,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.password)
                )
            },
            value = state.password,
            onValueChange = { onUpdate(state.toInput(password = it)) },
            enabled = !state.isRegistering,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.repeatPassword)
                )
            },
            value = state.repeatedPassword,
            onValueChange = { onUpdate(state.toInput(repeatedPassword = it)) },
            enabled = !state.isRegistering,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { onRegister(state.toInput()) },
            enabled = !state.isRegistering,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
    }
}