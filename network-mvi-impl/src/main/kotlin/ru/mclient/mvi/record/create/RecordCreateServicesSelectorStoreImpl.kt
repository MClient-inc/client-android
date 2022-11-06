package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class RecordCreateServicesSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: RecordCreateServicesSelectorStore.State?,
) : RecordCreateServicesSelectorStore,
    Store<RecordCreateServicesSelectorStore.Intent, RecordCreateServicesSelectorStore.State, RecordCreateServicesSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateServicesSelectorStoreImpl",
        initialState = state ?: RecordCreateServicesSelectorStore.State(
            isAvailable = true,
            isExpanded = false,
            services = emptyList(),
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<RecordCreateServicesSelectorStore.Intent, Nothing, RecordCreateServicesSelectorStore.State, RecordCreateServicesSelectorStore.State, RecordCreateServicesSelectorStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateServicesSelectorStore.Intent,
            getState: () -> RecordCreateServicesSelectorStore.State,
        ) {
            when (intent) {
                is RecordCreateServicesSelectorStore.Intent.Move -> {
                    dispatch(getState().copy(isExpanded = intent.isExpanded))
                }

                is RecordCreateServicesSelectorStore.Intent.Select -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            services = state.services + RecordCreateServicesSelectorStore.State.Service(
                                intent.id,
                                intent.title,
                                intent.cost,
                                "${intent.cost} ₽",
                            )
                        )
                    )
                }

                is RecordCreateServicesSelectorStore.Intent.DeleteById -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            services = state.services.filterNot { it.id == intent.id }
                        )
                    )
                }
            }
        }
    }

}