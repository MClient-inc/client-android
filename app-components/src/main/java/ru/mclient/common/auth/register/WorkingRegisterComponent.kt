package ru.mclient.common.auth.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mclient.common.auth.Register
import ru.mclient.common.auth.RegisterState

class WorkingRegisterComponent(
) : Register {



    override val state: StateFlow<RegisterState> = MutableStateFlow(
        RegisterState(
            email = "",
            isEmailValid = false,
            username = "",
            password = "",
            repeatedPassword = "",
            isError = false,
            isLoading = false,
        )
    )

    override fun onUpdate(
        email: String,
        username: String,
        password: String,
        repeatedPassword: String
    ) {

    }

    override fun onRegister(
        email: String,
        username: String,
        password: String,
        repeatedPassword: String
    ) {
    }

}