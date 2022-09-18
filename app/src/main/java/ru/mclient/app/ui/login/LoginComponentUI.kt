package ru.mclient.app.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ru.mclient.common.login.Login
import ru.mclient.common.login.LoginState
import ru.mclient.ui.Login
import ru.mclient.ui.LoginUIState

fun LoginState.toUI(): LoginUIState {
    return LoginUIState(
        username = username,
        password = password,
        isLoading = isLoading,
        error = if (isError) "Неизвестная ошибка" else null
    )
}

@Composable
fun LoginComponentUI(component: Login, modifier: Modifier) {
    val state = component.state.collectAsState().value.toUI()
    Login(
        state = state,
        onUpdate = { component.onUpdate(it.username, it.password) },
        onLogin = { component.onLogin(it.username, it.password) },
        modifier = modifier,
    )
}