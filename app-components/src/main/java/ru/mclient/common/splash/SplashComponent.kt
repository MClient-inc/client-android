package ru.mclient.common.splash

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.createCoroutineScope
import ru.mclient.common.utils.getStoreSavedState
import ru.mclient.mvi.splash.SplashStore

class SplashComponent(
    componentContext: DIComponentContext,
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit,
) : SplashHost, DIComponentContext by componentContext {

    private val componentScope = createCoroutineScope()

    private val store: SplashStore = getStoreSavedState("splash_store")

    init {
        store.states.onEach {
            when(it) {
                SplashStore.State.Authenticated -> onAuthenticated()
                SplashStore.State.Loading -> {}
                SplashStore.State.Unauthenticated -> onUnauthenticated()
            }
        }.launchIn(componentScope)
    }

}