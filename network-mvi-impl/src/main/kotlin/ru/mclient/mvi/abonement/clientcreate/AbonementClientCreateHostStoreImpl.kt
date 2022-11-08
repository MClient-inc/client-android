package ru.mclient.mvi.abonement.clientcreate

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.AddAbonementToClientInput

@Factory
class AbonementClientCreateHostStoreImpl(
    storeFactory: StoreFactory,
    abonementNetworkSource: AbonementNetworkSource,
    params: AbonementClientCreateHostStore.Params,
) : AbonementClientCreateHostStore,
    Store<AbonementClientCreateHostStore.Intent, AbonementClientCreateHostStore.State, AbonementClientCreateHostStore.Label> by storeFactory.create(
        name = "AbonementClientCreateHostStoreImpl",
        initialState = AbonementClientCreateHostStore.State(
            isLoading = false,
            isSuccess = false,
            isAvailable = true
        ),
        executorFactory = { StoreExecutor(abonementNetworkSource, params) },
        reducer = { it },
    ) {

    class StoreExecutor(
        private val abonementNetworkSource: AbonementNetworkSource,
        private val params: AbonementClientCreateHostStore.Params,
    ) :
        SyncCoroutineExecutor<AbonementClientCreateHostStore.Intent, Nothing, AbonementClientCreateHostStore.State, AbonementClientCreateHostStore.State, AbonementClientCreateHostStore.Label>() {

        override fun executeIntent(
            intent: AbonementClientCreateHostStore.Intent,
            getState: () -> AbonementClientCreateHostStore.State,
        ) {
            when (intent) {
                is AbonementClientCreateHostStore.Intent.Create -> {
                    val state = getState()
                    if (state.isLoading) {
                        return
                    }
                    if (state.isSuccess) {
                        dispatch(
                            AbonementClientCreateHostStore.State(
                                isLoading = false,
                                isSuccess = true,
                                isAvailable = false
                            )
                        )
                        return
                    }
                    dispatch(
                        AbonementClientCreateHostStore.State(
                            isLoading = true,
                            isSuccess = false,
                            isAvailable = false
                        )
                    )
                    scope.launch {
                        abonementNetworkSource.addAbonementForClient(
                            AddAbonementToClientInput(
                                clientId = params.clientId,
                                subabonementId = intent.subabonementId,
                            )
                        )
                        syncDispatch(
                            AbonementClientCreateHostStore.State(
                                isLoading = false,
                                isSuccess = true,
                                isAvailable = false
                            )
                        )
                    }
                }
            }
        }
    }

}