package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.GetAbonementsForClientInput
import kotlin.random.Random

@Factory
class ClientAbonementsSelectorStoreImpl(
    storeFactory: StoreFactory,
    state: ClientAbonementsSelectorStore.State?,
    abonementNetworkSource: AbonementNetworkSource,
) : ClientAbonementsSelectorStore,
    Store<ClientAbonementsSelectorStore.Intent, ClientAbonementsSelectorStore.State, ClientAbonementsSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateAbonementsSelectorStoreImpl",
        initialState = state ?: ClientAbonementsSelectorStore.State(
            clientId = null,
            isLoading = false,
            isAvailable = false,
            isExpanded = false,
            selectedAbonements = emptyMap(),
            clientAbonements = emptyList(),
            isRefreshing = false,
        ),
        executorFactory = { StoreExecutor(abonementNetworkSource) },
        reducer = { it },
    ) {

    class StoreExecutor(
        private val abonementNetworkSource: AbonementNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientAbonementsSelectorStore.Intent, Nothing, ClientAbonementsSelectorStore.State, ClientAbonementsSelectorStore.State, ClientAbonementsSelectorStore.Label>() {

        override fun executeIntent(
            intent: ClientAbonementsSelectorStore.Intent,
            getState: () -> ClientAbonementsSelectorStore.State,
        ) {
            when (intent) {
                is ClientAbonementsSelectorStore.Intent.Move -> {
                    dispatch(getState().copy(isExpanded = intent.isExpanded))
                }

                is ClientAbonementsSelectorStore.Intent.Select -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    val abonement =
                        state.clientAbonements.firstOrNull { it.id == intent.abonementId } ?: return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            selectedAbonements = state.selectedAbonements + (Random.nextInt() to abonement)
                        )
                    )
                }

                is ClientAbonementsSelectorStore.Intent.DeleteById -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            selectedAbonements = state.selectedAbonements - intent.id
                        )
                    )
                }

                is ClientAbonementsSelectorStore.Intent.ChangeClient -> {
                    val state = getState()
                    if (intent.clientId == state.clientId || state.isLoading)
                        return

                    val clientId = intent.clientId
                    dispatch(
                        state.copy(
                            isLoading = clientId != null,
                            isRefreshing = true,
                            isExpanded = false,
                            isAvailable = false,
                            selectedAbonements = emptyMap(),
                            clientAbonements = emptyList(),
                            clientId = clientId,
                        )
                    )
                    if (clientId != null)
                        scope.launch {
                            val abonements = abonementNetworkSource.getAbonementsForClient(
                                GetAbonementsForClientInput(clientId)
                            ).abonements.map {
                                ClientAbonementsSelectorStore.State.ClientAbonement(
                                    id = it.id,
                                    usages = it.usages,
                                    abonement = ClientAbonementsSelectorStore.State.Abonement(
                                        id = it.abonement.id,
                                        title = it.abonement.title,
                                        subabonement = ClientAbonementsSelectorStore.State.Subabonement(
                                            id = it.abonement.subabonement.id,
                                            title = it.abonement.subabonement.title,
                                            cost = it.abonement.subabonement.cost,
                                            maxUsages = it.abonement.subabonement.maxUsages,
                                        )
                                    )
                                )
                            }
                            val selected = intent.abonements?.let { selected ->
                                abonements.mapNotNull { if (it.id in selected) Random.nextInt() to it else null }
                                    .toMap()
                            }
                            syncDispatch(
                                state.copy(
                                    isLoading = false,
                                    isExpanded = false,
                                    isAvailable = true,
                                    isRefreshing = false,
                                    selectedAbonements = selected ?: emptyMap(),
                                    clientAbonements = abonements,
                                    clientId = clientId
                                )
                            )
                        }
                }

                ClientAbonementsSelectorStore.Intent.Refresh -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    val clientId = state.clientId
                    if (clientId != null) {
                        dispatch(
                            state.copy(
                                isLoading = true,
                                isRefreshing = true,
                            )
                        )
                        scope.launch {
                            val abonements = abonementNetworkSource.getAbonementsForClient(
                                GetAbonementsForClientInput(clientId)
                            )
                            syncDispatch(
                                state.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    selectedAbonements = emptyMap(),
                                    clientAbonements = abonements.abonements.map {
                                        ClientAbonementsSelectorStore.State.ClientAbonement(
                                            id = it.id,
                                            usages = it.usages,
                                            abonement = ClientAbonementsSelectorStore.State.Abonement(
                                                id = it.abonement.id,
                                                title = it.abonement.title,
                                                subabonement = ClientAbonementsSelectorStore.State.Subabonement(
                                                    id = it.abonement.subabonement.id,
                                                    title = it.abonement.subabonement.title,
                                                    cost = it.abonement.subabonement.cost,
                                                    maxUsages = it.abonement.subabonement.maxUsages,
                                                )
                                            )
                                        )
                                    },
                                    clientId = clientId
                                )
                            )
                        }
                    } else {
                        dispatch(
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                            )
                        )
                    }

                }
            }
        }
    }

}