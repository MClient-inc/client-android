package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.record.CreateRecordInput
import ru.mclient.network.record.RecordsNetworkSource

@Factory
class RecordCreateStoreImpl(
    storeFactory: StoreFactory,
    recordsNetworkSource: RecordsNetworkSource,
    params: RecordCreateStore.Params,
) : RecordCreateStore,
    Store<RecordCreateStore.Intent, RecordCreateStore.State, RecordCreateStore.Label> by storeFactory.create(
        name = "RecordCreateStoreImpl",
        initialState = RecordCreateStore.State(isLoading = false, isSuccess = false),
        executorFactory = { StoreExecutor(recordsNetworkSource, params) },
        reducer = { it },
    ) {

    class StoreExecutor(
        private val recordsNetworkSource: RecordsNetworkSource,
        private val params: RecordCreateStore.Params,
    ) :
        SyncCoroutineExecutor<RecordCreateStore.Intent, Nothing, RecordCreateStore.State, RecordCreateStore.State, RecordCreateStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateStore.Intent,
            getState: () -> RecordCreateStore.State,
        ) {
            when (intent) {
                is RecordCreateStore.Intent.Create -> {
                    val state = getState()
                    if (state.isLoading) {
                        return
                    }
                    if (state.isSuccess) {
                        dispatch(RecordCreateStore.State(isLoading = false, isSuccess = true))
                        return
                    }
                    dispatch(RecordCreateStore.State(isLoading = true, isSuccess = false))
                    scope.launch {
                        recordsNetworkSource.createRecord(
                            CreateRecordInput(
                                companyId = params.companyId,
                                clientId = intent.clientId,
                                staffId = intent.staffId,
                                dateTime = intent.dateTime,
                                services = intent.servicesIds,
                            )
                        )
                        syncDispatch(RecordCreateStore.State(isLoading = false, isSuccess = true))
                    }
                }
            }
        }
    }

}