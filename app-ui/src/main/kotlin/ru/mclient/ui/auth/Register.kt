package ru.mclient.ui.auth

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

@Parcelize
data class RegisterPageState(
    val email: String,
    val username: String,
    val password: String,
    val repeatedPassword: String,
    val isLoading: Boolean,
    val error: String?,
    val isEmailValid: Boolean,
) : Parcelable

class RegisterUIInput(
    val email: String,
    val username: String,
    val password: String,
    val repeatedPassword: String,
)

fun RegisterPageState.toInput(
    mail: String = this.email,
    username: String = this.username,
    password: String = this.password,
    repeatedPassword: String = this.repeatedPassword,
): RegisterUIInput {
    return RegisterUIInput(mail, username, password, repeatedPassword)
}

@Composable
fun Register(
    state: RegisterPageState,
    onUpdate: (RegisterUIInput) -> Unit,
    onRegister: (RegisterUIInput) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        if (!state.error.isNullOrEmpty())
            Text(
                text = state.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
            )
        val focusManager = LocalFocusManager.current
        DesignedTextField(
            label = stringResource(id = R.string.email),
            value = state.email,
            onValueChange = { onUpdate(state.toInput(mail = it)) },
            enabled = !state.isLoading,
            isError = !state.isEmailValid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedTextField(
            label = stringResource(id = R.string.username),
            value = state.username,
            onValueChange = { onUpdate(state.toInput(username = it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            enabled = !state.isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        DesignedTextField(
            label = stringResource(id = R.string.password),
            value = state.password,
            onValueChange = { onUpdate(state.toInput(password = it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            enabled = !state.isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedTextField(
            label = stringResource(id = R.string.repeatPassword),
            value = state.repeatedPassword,
            onValueChange = { onUpdate(state.toInput(repeatedPassword = it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            enabled = !state.isLoading,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedButton(
            text = stringResource(id = R.string.register).toDesignedString(),
            onClick = { onRegister(state.toInput()) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}