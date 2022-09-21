package ru.mclient.common.auth.register

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mclient.common.auth.Register
import ru.mclient.common.auth.RegisterState
import ru.mclient.common.utils.CoroutineInstance

class WorkingRegisterComponent(
    componentContext: ComponentContext,
    private val client: HttpClient
) : Register, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getOrCreate { RemoteRegisterViewModel(client) }

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
    ) : CoroutineInstance() {


        val state: MutableStateFlow<RegisterState> =
            MutableStateFlow(
                RegisterState(
                    email = "",
                    isEmailValid = true,
                    username = "",
                    password = "",
                    repeatedPassword = "",
                    isLoading = false,
                    isError = false
                )
            )

        fun onUpdate(email: String, username: String, password: String, repeatedPassword: String) {
            if (state.value.isLoading) return

            var trimmedEmail = email.filterNot(Char::isWhitespace)
            if (trimmedEmail.length > 256) {
                trimmedEmail = trimmedEmail.slice(0 until 256)
            }
            state.value = state.value.copy(
                email = trimmedEmail,
                isEmailValid = EMAIL_ADDRESS.matcher(trimmedEmail).matches(),
                username = username.filterNot(Char::isWhitespace),
                password = password.filterNot(Char::isWhitespace),
                repeatedPassword = repeatedPassword.filterNot(Char::isWhitespace),
                isError = false
            )
        }

        fun onRegister(
            email: String,
            username: String,
            password: String,
            repeatedPassword: String
        ) {
            if (state.value.isLoading) return
            val trimmedEmail = email.filterNot(Char::isWhitespace)
            state.value = state.value.copy(
                email = trimmedEmail,
                isEmailValid = EMAIL_ADDRESS.matcher(trimmedEmail).matches(),
                username = username.filterNot(Char::isWhitespace),
                password = password.filterNot(Char::isWhitespace),
                repeatedPassword = repeatedPassword.filterNot(Char::isWhitespace),
                isError = false,
                isLoading = true,
            )
            scope.launch {
                try {
                    val response = client.post("/register")
                    if (response.status.isSuccess()) {
                        TODO("register success")
                    } else {
                        state.value = state.value.copy(isError = true, isLoading = false)
                    }
                } catch (ea: Exception) {
                    state.value = state.value.copy(isError = true, isLoading = false)
                }
            }
        }
    }
}