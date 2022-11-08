package ru.mclient.mvi.client.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.GetAbonementsForClientInput
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.GetClientByIdInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ClientProfileStoreImpl(
    storeFactory: StoreFactory,
    params: ClientProfileStore.Params,
    clientSource: ClientNetworkSource,
    abonementSource: AbonementNetworkSource,
) : ClientProfileStore,
    Store<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label> by storeFactory.create(
        name = "ClientProfileStoreImpl",
        initialState = ClientProfileStore.State(
            client = null,
            abonements = null,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, clientSource, abonementSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    client = ClientProfileStore.State.Client(
                        id = message.client.id,
                        name = message.client.name,
                        phone = message.client.phone
                    ),
                    abonements = message.abonements.map {
                        ClientProfileStore.State.ClientAbonement(
                            id = it.id,
                            usages = it.usages,
                            abonement = ClientProfileStore.State.Abonement(
                                title = it.abonement.title,
                                subabonement = ClientProfileStore.State.Subabonement(
                                    title = it.abonement.subabonement.title,
                                    maxUsages = it.abonement.subabonement.maxUsages
                                )
                            )
                        )
                    },
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
        private val abonementSource: AbonementNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientProfileStore.Intent, Action, ClientProfileStore.State, Message, ClientProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ClientProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadClient(params.clientId)
            }
        }

        override fun executeIntent(
            intent: ClientProfileStore.Intent,
            getState: () -> ClientProfileStore.State,
        ) {
            when (intent) {
                ClientProfileStore.Intent.Refresh -> loadClient(params.clientId)
            }
        }

        private fun loadClient(clientId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = clientSource.getClientById(GetClientByIdInput(clientId))
                    val abonements = abonementSource.getAbonementsForClient(
                        GetAbonementsForClientInput((clientId))
                    )
                    dispatch(
                        Message.Loaded(
                            Message.Loaded.Client(
                                id = response.id,
                                name = response.name,
                                phone = response.phone
                            ),
                            abonements.abonements.map {
                                Message.Loaded.ClientAbonement(
                                    id = it.id,
                                    usages = it.usages,
                                    abonement = Message.Loaded.Abonement(
                                        title = it.abonement.title,
                                        subabonement = Message.Loaded.Subabonement(
                                            title = it.abonement.subabonement.title,
                                            maxUsages = it.abonement.subabonement.maxUsages,
                                        )
                                    )
                                )
                            }
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
            val abonements: List<ClientAbonement>,
        ) : Message() {
            class Client(
                val id: Long,
                val name: String,
                val phone: String,
            )

            class ClientAbonement(
                val id: Long,
                val usages: Int,
                val abonement: Abonement,
            )

            class Abonement(
                val title: String,
                val subabonement: Subabonement,
            )

            class Subabonement(
                val title: String,
                val maxUsages: Int,
            )

        }

    }
}