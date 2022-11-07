package ru.mclient.mvi.client.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.client.create.toPhoneFormat
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.GetClientsForCompanyInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ClientsListForCompanyStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    params: ClientsListForCompanyStore.Params,
    clientNetworkSource: ClientNetworkSource
) : ClientsListForCompanyStore,
    Store<ClientsListForCompanyStore.Intent, ClientsListForCompanyStore.State, ClientsListForCompanyStore.Label> by storeFactory.create(
        name = "ClientsListForCompanyStoreImpl",
        initialState = ClientsListForCompanyStore.State(
            clients = emptyList(),
            isLoading = true,
            isFailure = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, clientNetworkSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    clients = message.clients.map { client ->
                        ClientsListForCompanyStore.State.Client(
                            id = client.id,
                            title = client.title,
                            phone = client.phone.toPhoneFormat()
                        )
                    },
                    isLoading = false,
                    isFailure = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: ClientsListForCompanyStore.Params,
        private val clientNetworkSource: ClientNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientsListForCompanyStore.Intent, Action, ClientsListForCompanyStore.State, Message, ClientsListForCompanyStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> ClientsListForCompanyStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadClients(params.companyId)
            }
        }

        override fun executeIntent(
            intent: ClientsListForCompanyStore.Intent,
            getState: () -> ClientsListForCompanyStore.State
        ) {
            when (intent) {
                is ClientsListForCompanyStore.Intent.Refresh ->
                    loadClients(params.companyId)
            }
        }

        private fun loadClients(
            companyId: Long,
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = clientNetworkSource.findClientsForCompany(
                        GetClientsForCompanyInput(companyId)
                    )
                    syncDispatch(
                        Message.Loaded(
                            response.clients.map { client ->
                                Message.Loaded.Client(
                                    id = client.id,
                                    title = client.title,
                                    phone = client.phone,
                                )
                            },
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
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
            val clients: List<Client>,
        ) : Message() {
            class Client(
                val id: Long,
                val title: String,
                val phone: String,
            )
        }

    }
}