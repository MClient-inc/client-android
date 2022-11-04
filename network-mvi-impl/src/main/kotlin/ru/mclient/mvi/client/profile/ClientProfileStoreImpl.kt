package ru.mclient.mvi.client.profile

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
class ClientProfileStoreImpl(
    storeFactory: StoreFactory,
    params: ClientProfileStore.Params,
    clientSource: ClientNetworkSource,
) : ClientProfileStore,
    Store<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label> by storeFactory.create(
        name = "ClientProfileStoreImpl",
        initialState = ClientProfileStore.State(
            null,
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
                    ClientProfileStore.State.Client(
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
        private val params: ClientProfileStore.Params,
        private val clientSource: ClientNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientProfileStore.Intent, Action, ClientProfileStore.State, Message, ClientProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ClientProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadStaff(params.clientId)
            }
        }

        override fun executeIntent(
            intent: ClientProfileStore.Intent,
            getState: () -> ClientProfileStore.State
        ) {
            when (intent) {
                ClientProfileStore.Intent.Refresh -> loadStaff(params.clientId)
            }
        }

        private fun loadStaff(clientId: Long) {
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
                            )
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
            val client: Client
        ) : Message() {
            class Client(
                val id: Long,
                val name: String,
                val phone: String
            )
        }

    }
}