package ru.mclient.mvi.auth

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.local.auth.AuthLocalSource
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.account.AccountNetworkSource
import ru.mclient.network.auth.AuthNetworkSource
import ru.mclient.network.auth.GetTokenFromCodeInput

@Factory
class OAuthStoreImpl(
    storeFactory: StoreFactory,
    dispatcher: CoroutineDispatcher,
    authNetworkSource: AuthNetworkSource,
    accountNetworkSource: AccountNetworkSource,
    authLocalSource: AuthLocalSource,
) :
    OAuthLoginStore,
    Store<OAuthLoginStore.Intent, OAuthLoginStore.State, OAuthLoginStore.Label> by storeFactory.create(
        name = "OAuthLoginStoreImpl",
        initialState = OAuthLoginStore.State.Empty,
        executorFactory = {
            Executor(dispatcher, authNetworkSource, authLocalSource, accountNetworkSource)
        },
        reducer = Reducer<OAuthLoginStore.State, Message> { message ->
            when (message) {
                Message.OAuthPageStarted -> OAuthLoginStore.State.OAuthPage(System.currentTimeMillis() + 10000)
                Message.Failed -> OAuthLoginStore.State.Failure
                Message.LoginAccount -> OAuthLoginStore.State.TokensLoading
                is Message.Success -> OAuthLoginStore.State.Success(
                    id = message.id,
                    username = message.username,
                    name = message.name,
                    avatar = message.avatar,
                )
            }
        }
    ) {

    class Executor(
        dispatcher: CoroutineDispatcher,
        private val authNetworkSource: AuthNetworkSource,
        private val authLocalSource: AuthLocalSource,
        private val accountNetworkSource: AccountNetworkSource
    ) :
        SyncCoroutineExecutor<OAuthLoginStore.Intent, OAuthLoginStore.Intent, OAuthLoginStore.State, Message, OAuthLoginStore.Label>(
            dispatcher
        ) {

        override fun executeAction(
            action: OAuthLoginStore.Intent,
            getState: () -> OAuthLoginStore.State
        ) {
            executeIntent(action, getState)
        }

        override fun executeIntent(
            intent: OAuthLoginStore.Intent,
            getState: () -> OAuthLoginStore.State
        ) {
            when (intent) {
                is OAuthLoginStore.Intent.OAuthPageCompleted -> {
                    dispatch(Message.LoginAccount)
                    scope.launch {
                        Log.d(
                            "NetworkAuthDebug",
                            "Get token from code with verifier ${intent.codeVerifier}"
                        )
                        val tokens =
                            try {
                                authNetworkSource.getTokensFromCode(
                                    GetTokenFromCodeInput(
                                        intent.code,
                                        intent.codeVerifier,
                                    )
                                )
                            } catch (e: Exception) {
                                syncDispatch(Message.Failed)
                                return@launch
                            }
                        authLocalSource.saveTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken,
                        )
                        val account =
                            try {
                                accountNetworkSource.getBaseCurrentProfileInfo()
                            } catch (e: Exception) {
                                syncDispatch(Message.Failed)
                                return@launch
                            }
                        syncDispatch(
                            Message.Success(
                                id = account.id,
                                name = account.name,
                                username = account.username,
                                avatar = account.avatar
                            )
                        )
                    }
                }

                OAuthLoginStore.Intent.StartOAuthPage -> {
                    dispatch(Message.OAuthPageStarted)
                }

                OAuthLoginStore.Intent.OAuthPageFailure -> {
                    dispatch(Message.Failed)
                }
            }
        }

    }


    sealed class Message {
        object OAuthPageStarted : Message()
        object Failed : Message()
        object LoginAccount : Message()
        data class Success(
            val id: Long,
            val name: String,
            val username: String,
            val avatar: String?
        ) : Message()
    }

}