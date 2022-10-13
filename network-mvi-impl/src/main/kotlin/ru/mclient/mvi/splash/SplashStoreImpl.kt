package ru.mclient.mvi.splash

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.account.AccountNetworkSource

@Factory
class SplashStoreImpl(
    storeFactory: StoreFactory,
    dispatcher: CoroutineDispatcher,
    state: SplashStore.State?,
    authLocalSource: AuthLocalSource,
    accountNetworkSource: AccountNetworkSource,
) : SplashStore,
    Store<SplashStore.Intent, SplashStore.State, SplashStore.Label> by storeFactory.create(
        name = "SplashStoreImpl",
        initialState = state ?: SplashStore.State.Loading,
        bootstrapper = coroutineBootstrapper {

            if (state == null || state is SplashStore.State.Loading) {
                dispatch(Action.LoadAccount)
            }
        },
        executorFactory = {
            Executor(
                dispatcher = dispatcher,
                authLocalSource = authLocalSource,
                accountNetworkSource = accountNetworkSource
            )
        },
        reducer = Reducer<SplashStore.State, Message> { message ->
            when (message) {
                is Message.Authenticated -> SplashStore.State.Authenticated(message.accountId)
                Message.Unauthenticated -> SplashStore.State.Unauthenticated
            }
        }
    ) {

    class Executor(
        dispatcher: CoroutineDispatcher,
        private val authLocalSource: AuthLocalSource,
        private val accountNetworkSource: AccountNetworkSource,
    ) :
        SyncCoroutineExecutor<SplashStore.Intent, Action.LoadAccount, SplashStore.State, Message, SplashStore.Label>(
            dispatcher,
        ) {

        override fun executeAction(action: Action.LoadAccount, getState: () -> SplashStore.State) {
            when (action) {
                is Action.LoadAccount -> {
                    scope.launch {
                        val tokens = authLocalSource.getTokens()
                        Log.d("DataStoreAuthLocalSource", "token $tokens")
                        if (tokens == null) {
                            syncDispatch(Message.Unauthenticated)
                            return@launch
                        }
                        try {
                            val account = accountNetworkSource.getBaseCurrentProfileInfo()
                            syncDispatch(Message.Authenticated(account.id))
                        }catch (e:Exception) {
                            syncDispatch(Message.Unauthenticated)
                            return@launch
                        }
                    }
                }
            }
        }

    }

    sealed class Message {
        data class Authenticated(val accountId: Long) : Message()
        object Unauthenticated : Message()
    }

    sealed class Action {
        object LoadAccount : Action()
    }
}