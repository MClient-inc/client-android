package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class RecordCreateDateSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: RecordCreateDateSelectorStore.State?,
) : RecordCreateDateSelectorStore,
    Store<RecordCreateDateSelectorStore.Intent, RecordCreateDateSelectorStore.State, RecordCreateDateSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateDateSelectorStoreImpl",
        initialState = state ?: RecordCreateDateSelectorStore.State(
            isAvailable = true,
            date = null,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<RecordCreateDateSelectorStore.Intent, Nothing, RecordCreateDateSelectorStore.State, RecordCreateDateSelectorStore.State, RecordCreateDateSelectorStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateDateSelectorStore.Intent,
            getState: () -> RecordCreateDateSelectorStore.State,
        ) {
            when (intent) {
                is RecordCreateDateSelectorStore.Intent.ChangeDate -> {
                    dispatch(RecordCreateDateSelectorStore.State(true, intent.date))
                }
            }
        }
    }

}