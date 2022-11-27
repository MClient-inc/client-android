package ru.mclient.ui.auth.register

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.auth.Register
import ru.mclient.common.auth.RegisterState
import ru.mclient.ui.auth.Register
import ru.mclient.ui.auth.RegisterPageState

fun RegisterState.toUI(): RegisterPageState {
    return RegisterPageState(
        email = email,
        username = username,
        password = password,
        repeatedPassword = repeatedPassword,
        isLoading = isLoading,
        error = if (isError) "Неизвестная ошибка" else null,
        isEmailValid = isEmailValid,
    )
}

@Composable
fun RegisterUI(component: Register, modifier: Modifier) {
    Register(
        state = component.state.toUI(),
        onUpdate = {
            component.onUpdate(
                email = it.email,
                username = it.username,
                password = it.password,
                repeatedPassword = it.repeatedPassword
            )
        },
        onRegister = {
            component.onRegister(
                email = it.email,
                username = it.username,
                password = it.password,
                repeatedPassword = it.repeatedPassword
            )
        },
        modifier = modifier
            .padding(10.dp),
    )
}