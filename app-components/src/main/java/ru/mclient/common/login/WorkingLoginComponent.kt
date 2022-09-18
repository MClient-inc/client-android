package ru.mclient.common.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mclient.common.utils.CoroutineInstance

class WorkingLoginComponent(
    componentContext: ComponentContext,
    private val client: HttpClient,
) : Login, ComponentContext by componentContext {

    private val store = instanceKeeper.getOrCreate { RemoteLoginViewModel(client) }

    override fun onUpdate(username: String, password: String) {
        store.onUpdate(username, password)
    }

    override fun onLogin(username: String, password: String) {
        store.onLogin(username, password)
    }

    override val state: StateFlow<LoginState>
        get() = store.state

    class RemoteLoginViewModel(
        private val client: HttpClient,
    ) : CoroutineInstance() {

        val state: MutableStateFlow<LoginState> =
            MutableStateFlow(
                LoginState(
                    username = "",
                    password = "",
                    isLoading = false,
                    isError = false,
                )
            )


        fun onUpdate(username: String, password: String) {
            if (state.value.isLoading) {
                return
            }
            state.value =
                state.value.copy(username = username, password = password, isError = false)
        }

        fun onLogin(username: String, password: String) {
            if (state.value.isLoading) {
                return
            }
            state.value = state.value.copy(isLoading = true)
            scope.launch {
                try {
                    val response = client.post("/auth")
                    if (response.status.isSuccess()) {
                        TODO()
                    } else {
                        state.value = state.value.copy(isError = true, isLoading = false)
                    }
                } catch (e: Exception) {
                    state.value = state.value.copy(isError = true, isLoading = false)
                }
            }
        }


        override fun onDestroy() {
        }
    }

}