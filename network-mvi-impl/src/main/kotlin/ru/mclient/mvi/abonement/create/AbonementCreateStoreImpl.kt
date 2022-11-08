package ru.mclient.mvi.abonement.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.CreateAbonementInput

@Factory
class AbonementCreateStoreImpl(
    storeFactory: StoreFactory,
    abonementNetworkSource: AbonementNetworkSource,
    params: AbonementCreateStore.Params,
) : AbonementCreateStore,
    Store<AbonementCreateStore.Intent, AbonementCreateStore.State, AbonementCreateStore.Label> by storeFactory.create(
        name = "AbonementCreateStoreImpl",
        initialState = AbonementCreateStore.State(
            isLoading = false,
            isSuccess = false,
            isAvailable = true,
            abonement = null,
        ),
        executorFactory = { StoreExecutor(abonementNetworkSource, params) },
        reducer = { it },
    ) {

    class StoreExecutor(
        private val abonementNetworkSource: AbonementNetworkSource,
        private val params: AbonementCreateStore.Params,
    ) :
        SyncCoroutineExecutor<AbonementCreateStore.Intent, Nothing, AbonementCreateStore.State, AbonementCreateStore.State, AbonementCreateStore.Label>() {

        override fun executeIntent(
            intent: AbonementCreateStore.Intent,
            getState: () -> AbonementCreateStore.State,
        ) {
            when (intent) {
                is AbonementCreateStore.Intent.Create -> {
                    val state = getState()
                    if (state.isLoading) {
                        return
                    }
                    if (state.isSuccess) {
                        dispatch(
                            state.copy(
                                isLoading = false,
                                isSuccess = true,
                                isAvailable = false
                            )
                        )
                        return
                    }
                    dispatch(
                        state.copy(
                            isLoading = true,
                            isSuccess = false,
                            isAvailable = false,
                        )
                    )
                    scope.launch {
                        val abonement = abonementNetworkSource.createAbonement(
                            CreateAbonementInput(
                                params.companyId,
                                intent.title,
                                intent.subabonements.map {
                                    CreateAbonementInput.Subabonement(
                                        title = it.title,
                                        usages = it.usages,
                                        cost = it.cost,
                                    )
                                },
                                intent.services
                            )
                        )
                        syncDispatch(
                            AbonementCreateStore.State(
                                isLoading = false,
                                isSuccess = true,
                                isAvailable = false,
                                abonement = AbonementCreateStore.State.Abonement(abonement.abonement.id),
                            )
                        )
                    }
                }
            }
        }
    }

}