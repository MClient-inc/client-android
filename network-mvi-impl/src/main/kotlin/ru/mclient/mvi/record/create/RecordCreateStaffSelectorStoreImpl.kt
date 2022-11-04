package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class RecordCreateStaffSelectorStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: RecordCreateStaffSelectorStore.State?,
) : RecordCreateStaffSelectorStore,
    Store<RecordCreateStaffSelectorStore.Intent, RecordCreateStaffSelectorStore.State, RecordCreateStaffSelectorStore.Label> by storeFactory.create(
        name = "RecordCreateStaffSelectorStoreImpl",
        initialState = state ?: RecordCreateStaffSelectorStore.State(
            isAvailable = false,
            isExpanded = false,
            selectedStaff = null,
            schedule = null,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<RecordCreateStaffSelectorStore.Intent, Nothing, RecordCreateStaffSelectorStore.State, RecordCreateStaffSelectorStore.State, RecordCreateStaffSelectorStore.Label>() {

        override fun executeIntent(
            intent: RecordCreateStaffSelectorStore.Intent,
            getState: () -> RecordCreateStaffSelectorStore.State,
        ) {
            when (intent) {
                is RecordCreateStaffSelectorStore.Intent.ChangeSchedule -> {
                    dispatch(
                        getState().copy(
                            isExpanded = false,
                            selectedStaff = null,
                            isAvailable = true,
                            schedule = intent.schedule,
                        )
                    )
                }

                is RecordCreateStaffSelectorStore.Intent.Move -> {
                    dispatch(getState().copy(isExpanded = intent.isExpanded))
                }

                is RecordCreateStaffSelectorStore.Intent.Select -> {
                    val state = getState()
                    if (!state.isAvailable)
                        return
                    dispatch(
                        state.copy(
                            isExpanded = false,
                            selectedStaff = RecordCreateStaffSelectorStore.State.Staff(
                                id = intent.staffId,
                                name = intent.staffName,
                            )
                        )
                    )
                }
            }
        }
    }

}