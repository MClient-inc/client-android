package ru.mclient.mvi.staff.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.staff.GetStaffByIdInput
import ru.mclient.network.staff.GetStaffScheduleByIdInput
import ru.mclient.network.staff.StaffNetworkSource
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class CompanyProfileStoreImpl(
    storeFactory: StoreFactory,
    params: StaffProfileStore.Params,
    staffSource: StaffNetworkSource,
) : StaffProfileStore,
    Store<StaffProfileStore.Intent, StaffProfileStore.State, StaffProfileStore.Label> by storeFactory.create(
        name = "CompanyProfileStoreImpl",
        initialState = StaffProfileStore.State(
            null,
            emptyList(),
            isFailure = false,
            isLoading = true,
            isRefreshing = false,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, staffSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    staff = StaffProfileStore.State.Staff(
                        id = message.staff.id,
                        name = message.staff.name,
                        codename = message.staff.codename,
                        role = message.staff.role,
                    ),
                    schedule = message.schedule.map {
                        StaffProfileStore.State.Schedule(
                            date = it.date,
                            start = it.start,
                            end = it.end
                        )
                    },
                    isFailure = false,
                    isLoading = false,
                    isRefreshing = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                    isRefreshing = staff != null,
                )
            }
        }
    ) {

    class Executor(
        private val params: StaffProfileStore.Params,
        private val staffSource: StaffNetworkSource,
    ) :
        SyncCoroutineExecutor<StaffProfileStore.Intent, Action, StaffProfileStore.State, Message, StaffProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> StaffProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadStaff(params.staffId)
            }
        }

        override fun executeIntent(
            intent: StaffProfileStore.Intent,
            getState: () -> StaffProfileStore.State,
        ) {
            when (intent) {
                StaffProfileStore.Intent.Refresh -> loadStaff(params.staffId)
            }
        }

        private fun loadStaff(staffId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = staffSource.getStaffById(GetStaffByIdInput(staffId))
                    val schedule = staffSource.getStaffSchedule(GetStaffScheduleByIdInput(staffId))
                    dispatch(
                        Message.Loaded(
                            staff = Message.Loaded.Staff(
                                id = response.id,
                                name = response.name,
                                codename = response.codename,
                                role = response.role,
                            ),
                            schedule = schedule.schedule.map {
                                StaffProfileStore.State.Schedule(
                                    date = it.date,
                                    start = it.start,
                                    end = it.end
                                )
                            }
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                }
            }
        }
    }

    sealed class Action {
        object FirstLoad : Action()
    }

    sealed class Message {

        object Failed : Message()

        object Loading : Message()

        class Loaded(
            val staff: Staff,
            val schedule: List<StaffProfileStore.State.Schedule>,
        ) : Message() {
            class Staff(
                val id: Long,
                val name: String,
                val codename: String,
                val role: String,
            )

            class Schedule(
                val date: LocalDate,
                val start: LocalTime,
                val end: LocalTime,
            )
        }

    }
}