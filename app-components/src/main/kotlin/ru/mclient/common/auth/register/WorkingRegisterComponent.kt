package ru.mclient.common.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import ru.mclient.common.auth.Register
import ru.mclient.common.auth.RegisterState

class WorkingRegisterComponent : Register {


    override val state: RegisterState by mutableStateOf(
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