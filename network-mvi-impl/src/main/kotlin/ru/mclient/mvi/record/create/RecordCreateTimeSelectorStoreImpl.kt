package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class RecordCreateTimeSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: RecordCreateTimeSelectorStore.State?,
) : RecordCreateTimeSelectorStore,
    Store<RecordCreateTimeSelectorStore.Intent, RecordCreateTimeSelectorStore.State, RecordCreateTimeSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateTimeSelectorStoreImpl",
        initialState = state ?: RecordCreateTimeSelectorStore.State(
            isAvailable = true,
            time = null,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<RecordCreateTimeSelectorStore.Intent, Nothing, RecordCreateTimeSelectorStore.State, RecordCreateTimeSelectorStore.State, RecordCreateTimeSelectorStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateTimeSelectorStore.Intent,
            getState: () -> RecordCreateTimeSelectorStore.State,
        ) {
            when (intent) {
                is RecordCreateTimeSelectorStore.Intent.ChangeTme -> {
                    dispatch(RecordCreateTimeSelectorStore.State(true, intent.time))
                }
            }
        }
    }

}