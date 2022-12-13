package ru.mclient.mvi.staff.schedule

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.staff.CreateStaffScheduleInput
import ru.mclient.network.staff.StaffNetworkSource

@Factory
class StaffScheduleEditStoreImpl(
    storeFactory: StoreFactory,
    staffSource: StaffNetworkSource,
    params: StaffScheduleEditStore.Params,
) : StaffScheduleEditStore,
    Store<StaffScheduleEditStore.Intent, StaffScheduleEditStore.State, StaffScheduleEditStore.Label> by storeFactory.create(
        name = "StaffScheduleEditStoreImpl",
        initialState = StaffScheduleEditStore.State(
            isLoading = false,
            isAvailable = true,
            selectedSchedule = listOf(),
            isSuccess = false,
        ),
        executorFactory = { StoreExecutor(staffSource, params) },
        reducer = { it },
    ) {

    class StoreExecutor(
        private val staffSource: StaffNetworkSource,
        private val params: StaffScheduleEditStore.Params,
    ) :
        SyncCoroutineExecutor<StaffScheduleEditStore.Intent, Nothing, StaffScheduleEditStore.State, StaffScheduleEditStore.State, StaffScheduleEditStore.Label>() {

        override fun executeIntent(
            intent: StaffScheduleEditStore.Intent,
            getState: () -> StaffScheduleEditStore.State,
        ) {
            when (intent) {
                StaffScheduleEditStore.Intent.Apply -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    dispatch(state.copy(isLoading = true, isAvailable = false))
                    scope.launch {
                        staffSource.createStaffSchedule(
                            CreateStaffScheduleInput(
                                params.staffId.toString(),
                                state.selectedSchedule.map {
                                    CreateStaffScheduleInput.Schedule(
                                        companyId = params.companyId.toString(),
                                        start = it.start,
                                        end = it.end
                                    )
                                }
                            )
                        )
                        dispatch(
                            state.copy(isLoading = false, isAvailable = false, isSuccess = true)
                        )
                    }

                }

                is StaffScheduleEditStore.Intent.Select -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    dispatch(
                        StaffScheduleEditStore.State(
                            selectedSchedule = state.selectedSchedule + StaffScheduleEditStore.State.Schedule(
                                start = intent.start,
                                end = intent.end
                            ),
                            isAvailable = true,
                            isLoading = false,
                            isSuccess = false,
                        )
                    )
                }
            }
        }
    }

}