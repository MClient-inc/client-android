package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class RecordCreateClientSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: RecordCreateClientSelectorStore.State?,
) : RecordCreateClientSelectorStore,
    Store<RecordCreateClientSelectorStore.Intent, RecordCreateClientSelectorStore.State, RecordCreateClientSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateClientSelectorStoreImpl",
        initialState = state ?: RecordCreateClientSelectorStore.State(
            isAvailable = true,
            isExpanded = false,
            selectedClient = null,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<RecordCreateClientSelectorStore.Intent, Nothing, RecordCreateClientSelectorStore.State, RecordCreateClientSelectorStore.State, RecordCreateClientSelectorStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateClientSelectorStore.Intent,
            getState: () -> RecordCreateClientSelectorStore.State,
        ) {
            when (intent) {
                is RecordCreateClientSelectorStore.Intent.Move -> {
                    dispatch(getState().copy(isExpanded = intent.isExpanded))
                }

                is RecordCreateClientSelectorStore.Intent.Select -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            selectedClient = RecordCreateClientSelectorStore.State.Client(
                                id = intent.clientId,
                                name = intent.clientName,
                            )
                        )
                    )
                }
            }
        }
    }

}