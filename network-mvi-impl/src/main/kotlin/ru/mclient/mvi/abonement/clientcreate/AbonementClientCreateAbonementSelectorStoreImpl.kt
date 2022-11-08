package ru.mclient.mvi.abonement.clientcreate

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class AbonementClientCreateAbonementSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: AbonementClientCreateAbonementSelectorStore.State?,
) : AbonementClientCreateAbonementSelectorStore,
    Store<AbonementClientCreateAbonementSelectorStore.Intent, AbonementClientCreateAbonementSelectorStore.State, AbonementClientCreateAbonementSelectorStore.Label> by storeFactory.create(
        name = "AbonementClientCreateAbonementSelectorStoreImpl",
        initialState = state ?: AbonementClientCreateAbonementSelectorStore.State(
            isAvailable = true,
            isExpanded = false,
            abonement = null,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<AbonementClientCreateAbonementSelectorStore.Intent, Nothing, AbonementClientCreateAbonementSelectorStore.State, AbonementClientCreateAbonementSelectorStore.State, AbonementClientCreateAbonementSelectorStore.Label>() {

        override fun executeIntent(
            intent: AbonementClientCreateAbonementSelectorStore.Intent,
            getState: () -> AbonementClientCreateAbonementSelectorStore.State,
        ) {
            when (intent) {
                is AbonementClientCreateAbonementSelectorStore.Intent.Move -> {
                    dispatch(getState().copy(isExpanded = intent.isExpanded))
                }

                is AbonementClientCreateAbonementSelectorStore.Intent.Select -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            abonement = AbonementClientCreateAbonementSelectorStore.State.Abonement(
                                id = intent.abonement.id,
                                title = intent.abonement.title,
                                subabonement = AbonementClientCreateAbonementSelectorStore.State.Subabonement(
                                    id = intent.abonement.subabonement.id,
                                    title = intent.abonement.subabonement.title,
                                    cost = intent.abonement.subabonement.cost,
                                )
                            )
                        )
                    )
                }
            }
        }
    }

}