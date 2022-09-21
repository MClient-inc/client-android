package ru.mclient.app.ui.auth.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.auth.Register
import ru.mclient.common.auth.RegisterState
import ru.mclient.ui.auth.Register
import ru.mclient.ui.auth.RegisterUIState

fun RegisterState.toUI(): RegisterUIState {
    return RegisterUIState(
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
    val state = component.state.collectAsState().value.toUI()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Register(
            state = state,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        )
    }
}