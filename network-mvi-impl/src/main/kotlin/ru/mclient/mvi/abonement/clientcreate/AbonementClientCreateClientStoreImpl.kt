package ru.mclient.mvi.abonement.clientcreate

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.GetClientByIdInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class AbonementClientCreateClientStoreImpl(
    storeFactory: StoreFactory,
    params: AbonementClientCreateClientStore.Params,
    clientSource: ClientNetworkSource,
) : AbonementClientCreateClientStore,
    Store<AbonementClientCreateClientStore.Intent, AbonementClientCreateClientStore.State, AbonementClientCreateClientStore.Label> by storeFactory.create(
        name = "AbonementClientCreateClientStoreImpl",
        initialState = AbonementClientCreateClientStore.State(
            client = null,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, clientSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    AbonementClientCreateClientStore.State.Client(
                        id = message.client.id,
                        name = message.client.name,
                        phone = message.client.phone
                    ),
                    isFailure = false,
                    isLoading = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: AbonementClientCreateClientStore.Params,
        private val clientSource: ClientNetworkSource,
    ) :
        SyncCoroutineExecutor<AbonementClientCreateClientStore.Intent, Action, AbonementClientCreateClientStore.State, Message, AbonementClientCreateClientStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> AbonementClientCreateClientStore.State,
        ) {
            when (action) {
                Action.FirstLoad -> loadClient(params.clientId)
            }
        }

        override fun executeIntent(
            intent: AbonementClientCreateClientStore.Intent,
            getState: () -> AbonementClientCreateClientStore.State,
        ) {
            when (intent) {
                AbonementClientCreateClientStore.Intent.Refresh -> loadClient(params.clientId)
            }
        }

        private fun loadClient(clientId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = clientSource.getClientById(GetClientByIdInput(clientId))
                    dispatch(
                        Message.Loaded(
                            Message.Loaded.Client(
                                id = response.id,
                                name = response.name,
                                phone = response.phone
                            ),
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                }
            }
        }
    }

    sealed class Action {
        object FirstLoad : Action()
    }

    sealed class Message {
        object Failed : Message()
        object Loading : Message()
        class Loaded(
            val client: Client,
        ) : Message() {
            class Client(
                val id: Long,
                val name: String,
                val phone: String,
            )

        }

    }
}