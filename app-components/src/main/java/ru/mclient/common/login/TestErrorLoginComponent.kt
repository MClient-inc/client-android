package ru.mclient.common.login

import kotlinx.coroutines.flow.MutableStateFlow

class TestErrorLoginComponent : Login {
    override val state: MutableStateFlow<LoginState> =
        MutableStateFlow(
            LoginState(
                username = "",
                password = "",
                isLoading = false,
                isError = false,
            )
        )

    override fun onUpdate(username: String, password: String) {
        state.value = state.value.copy(username = username, password = password)
    }

    override fun onLogin(username: String, password: String) {
        state.value = state.value.copy(isError = true)
    }

}