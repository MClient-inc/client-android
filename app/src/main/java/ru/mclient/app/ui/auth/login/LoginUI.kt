package ru.mclient.app.ui.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.auth.Login
import ru.mclient.common.auth.LoginState
import ru.mclient.ui.auth.Login
import ru.mclient.ui.auth.LoginUIState

fun LoginState.toUI(): LoginUIState {
    return LoginUIState(
        username = username,
        password = password,
        isLoading = isLoading,
        error = if (isError) "Неизвестная ошибка" else null
    )
}

@Composable
fun LoginUI(component: Login, modifier: Modifier) {
    val state = component.state.collectAsState().value.toUI()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Login(
            state = state,
            onUpdate = { component.onUpdate(it.username, it.password) },
            onLogin = { component.onLogin(it.username, it.password) },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
        )
    }
}