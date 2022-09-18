package ru.mclient.common.register

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mclient.common.login.Register
import ru.mclient.common.login.RegisterState

class TestErrorRegisterComponent : Register {

    override val state: MutableStateFlow<RegisterState> =
        MutableStateFlow(
            RegisterState(
                mail = "",
                username = "",
                password = "",
                repeatedPassword = "",
                isRegistering = false,
                isError = false
            )
        )

    override fun onUpdate(mail: String, username: String, password: String, repeatedPassword : String) {
        state.value = state.value.copy(
            mail = mail,
            username = username,
            password = password,
            repeatedPassword = repeatedPassword
        )
    }

    override fun onRegister(mail: String, username: String, password: String, repeatedPassword: String) {
        state.value = state.value.copy(isError = true)
    }
}