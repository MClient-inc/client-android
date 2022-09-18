package ru.mclient.app.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ru.mclient.common.login.Register
import ru.mclient.common.login.RegisterState
import ru.mclient.ui.Register
import ru.mclient.ui.RegisterUIState

fun RegisterState.toUI(): RegisterUIState {
    return RegisterUIState(
        mail = mail,
        username = username,
        password = password,
        repeatedPassword = repeatedPassword,
        isRegistering = isRegistering,
        error = if (isError) "Неизвестная ошибка" else null
    )
}

@Composable
fun RegisterComponentUI(component: Register, modifier: Modifier) {
    val state = component.state.collectAsState().value.toUI()
    Register(
        state = state,
        onUpdate = { component.onUpdate(it.mail, it.username, it.password, it.repeatedPassword) },
        onRegister = {
            component.onRegister(
                mail = it.mail,
                username = it.username,
                password = it.password,
                repeatedPassword = it.repeatedPassword
            )
        },
        modifier = modifier
    )
}