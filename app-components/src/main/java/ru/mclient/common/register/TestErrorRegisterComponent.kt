package ru.mclient.common.register

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mclient.common.login.Register
import ru.mclient.common.login.RegisterState

class TestErrorRegisterComponent : Register {

    override val state: MutableStateFlow<RegisterState> =
        MutableStateFlow(
            RegisterState(
                email = "",
                isEmailValid = true,
                username = "",
                password = "",
                repeatedPassword = "",
                isRegistering = false,
                isError = false
            )
        )

    override fun onUpdate(email: String, username: String, password: String, repeatedPassword : String) {
        state.value = state.value.copy(
            email = email,
            username = username,
            password = password,
            repeatedPassword = repeatedPassword
        )
    }

    override fun onRegister(email: String, username: String, password: String, repeatedPassword: String) {
        state.value = state.value.copy(isError = true)
    }
}