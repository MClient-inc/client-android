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

@Factory
class SplashStoreImpl(
    storeFactory: StoreFactory,
    dispatcher: CoroutineDispatcher,
    state: SplashStore.State?,
    authLocalSource: AuthLocalSource,
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
                dispatcher,
                authLocalSource,
            )
        },
        reducer = Reducer<SplashStore.State, Message> {
            when (it) {
                Message.Authenticated -> SplashStore.State.Authenticated
                Message.Unauthenticated -> SplashStore.State.Unauthenticated
            }
        }
    ) {

    class Executor(
        dispatcher: CoroutineDispatcher,
        private val authLocalSource: AuthLocalSource,
    ) :
        SyncCoroutineExecutor<SplashStore.Intent, Action.LoadAccount, SplashStore.State, Message, SplashStore.Label>(
            dispatcher,
        ) {

        override fun executeAction(action: Action.LoadAccount, getState: () -> SplashStore.State) {
            when (action) {
                is Action.LoadAccount -> {
                    scope.launch {
                        val account = authLocalSource.getTokens()

                        Log.d("DataStoreAuthLocalSource", "token $account")
                        if (account == null) {
                            syncDispatch(Message.Unauthenticated)
                        } else {
                            syncDispatch(Message.Authenticated)
                        }
                    }
                }
            }
        }

    }


    sealed class Message {
        object Authenticated : Message()
        object Unauthenticated : Message()
    }

    sealed class Action {
        object LoadAccount : Action()
    }
}