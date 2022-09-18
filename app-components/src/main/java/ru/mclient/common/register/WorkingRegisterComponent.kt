package ru.mclient.common.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.validator.routines.EmailValidator
import ru.mclient.common.login.Register
import ru.mclient.common.login.RegisterState
import ru.mclient.common.utils.CoroutineInstance

class WorkingRegisterComponent(
    componentContext: ComponentContext,
    private val client: HttpClient
) : Register, ComponentContext by componentContext {

    private val store = instanceKeeper.getOrCreate { RemoteRegisterViewModel(client, EmailValidator.getInstance()) }

    override val state: StateFlow<RegisterState>
        get() = store.state

    override fun onUpdate(
        email: String,
        username: String,
        password: String,
        repeatedPassword: String
    ) {
        store.onUpdate(email, username, password, repeatedPassword)
    }

    override fun onRegister(
        email: String,
        username: String,
        password: String,
        repeatedPassword: String
    ) {
        store.onRegister(email, username, password, repeatedPassword)
    }

    class RemoteRegisterViewModel(
        private val client: HttpClient,
        private val validator: EmailValidator
    ) : CoroutineInstance() {
        override fun onDestroy() {}

        val state: MutableStateFlow<RegisterState> =
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

        fun onUpdate(email: String, username: String, password: String, repeatedPassword: String) {
            if (state.value.isRegistering) return

            state.value = state.value.copy(
                email = email,
                isEmailValid = validator.isValid(email),
                username = username,
                password = password,
                repeatedPassword = repeatedPassword,
                isError = false
            )
        }

        fun onRegister(
            email: String,
            username: String,
            password: String,
            repeatedPassword: String
        ) {
            if (state.value.isRegistering) return
            state.value = state.value.copy(isRegistering = true)
            scope.launch {
                try {
                    val response = client.post("/register")
                    if (response.status.isSuccess()) {
                        TODO("register success")
                    } else {
                        state.value = state.value.copy(isError = true, isRegistering = false)
                    }
                } catch (ea: Exception) {
                    state.value = state.value.copy(isError = true, isRegistering = false)
                }
            }
        }
    }
}