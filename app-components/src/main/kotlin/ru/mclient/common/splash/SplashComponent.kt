package ru.mclient.common.splash

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getStoreSavedState
import ru.mclient.mvi.splash.SplashStore

class SplashComponent(
    componentContext: DIComponentContext,
    onAuthenticated: (Long) -> Unit,
    onUnauthenticated: () -> Unit,
) : SplashHost, DIComponentContext by componentContext {

    private val store: SplashStore = getStoreSavedState("splash_store")

    init {
        store.states.onEach {
            when(it) {
                is SplashStore.State.Authenticated -> onAuthenticated(it.accountId)
                SplashStore.State.Loading -> {}
                SplashStore.State.Unauthenticated -> onUnauthenticated()
            }
        }.launchIn(componentScope)
    }

}