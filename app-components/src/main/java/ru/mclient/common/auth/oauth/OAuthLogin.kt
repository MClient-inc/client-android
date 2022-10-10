package ru.mclient.common.auth.oauth

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.get
import ru.mclient.common.DIComponentContext
import ru.mclient.common.auth.ExternalLogin
import ru.mclient.common.auth.ExternalLoginState
import ru.mclient.common.utils.getStore
import ru.mclient.mvi.auth.OAuthLoginStore

class OAuthLogin(
    componentContext: DIComponentContext,
    private val onAuthorized: (Long) -> Unit,
) : ExternalLogin,
    DIComponentContext by componentContext {

    private val oAuthRequest: OAuthRequest = get()

    private val registry: ActivityResultRegistry = get()

    private val loginRegister = registry.register(
        "login_result",
        ActivityResultContracts.StartActivityForResult()
    ) { it.data?.let(oAuthRequest::onAuthorizationCompleted) }

    private val store: OAuthLoginStore = getStore()

    override val state: StateFlow<ExternalLoginState> = store.states.map {
        it.toState()
    }.stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())

    init {
        lifecycle.doOnCreate {
            if (store.state is OAuthLoginStore.State.Empty) {
                openPage()
            }
        }
        lifecycle.doOnDestroy {
            loginRegister.unregister()
        }
        oAuthRequest.state.onEach {
            when (it) {
                is AuthorizationState.Failure ->
                    store.accept(OAuthLoginStore.Intent.OAuthPageFailure)

                is AuthorizationState.Success ->
                    store.accept(
                        OAuthLoginStore.Intent.OAuthPageCompleted(
                            it.code,
                            it.codeVerifier,
                        )
                    )
            }
        }.launchIn(componentScope)
    }

    private fun openPage() {
        loginRegister.launch(oAuthRequest.getOpenPageIntent())
    }

    private fun OAuthLoginStore.State.toState(): ExternalLoginState {
        return when (this) {
            OAuthLoginStore.State.Empty -> {
                ExternalLoginState(
                    message = "Скоро вас перенаправит в браузер...",
                    isRetryAvailable = true,
                    timerEndAt = System.currentTimeMillis() + 10000,
                )
            }

            OAuthLoginStore.State.Failure -> {
                ExternalLoginState(
                    message = "Непредвиденная ошибка",
                    isRetryAvailable = true,
                    timerEndAt = System.currentTimeMillis(),
                )
            }

            is OAuthLoginStore.State.OAuthPage -> {
                ExternalLoginState(
                    message = "Браузер уже должен быть открыт",
                    isRetryAvailable = true,
                    timerEndAt = timerEndAt,
                )
            }

            is OAuthLoginStore.State.Success ->
                ExternalLoginState(
                    message = "Успешно",
                    isRetryAvailable = false,
                    timerEndAt = 0,
                    account = ExternalLoginState.Account(
                        id = id,
                        username = username,
                        name = name,
                        avatar = avatar,
                    )
                )

            OAuthLoginStore.State.TokensLoading ->
                ExternalLoginState(
                    message = "Получение данных из аккаунта",
                    isRetryAvailable = false,
                    timerEndAt = 0,
                )
        }
    }


    override fun onRetry() {
        if (!state.value.isRetryAvailable)
            return
        openPage()
        store.accept(OAuthLoginStore.Intent.StartOAuthPage)
    }

    override fun onAuthenticated() {
        val id = state.value.account?.id ?: return
        onAuthorized.invoke(id)
    }
}
