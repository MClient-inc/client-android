package ru.mclient.mvi.staff.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.staff.GetStaffForCompanyAndScheduleInput
import ru.mclient.network.staff.StaffNetworkSource
import java.time.LocalDateTime

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class StaffListForCompanyAndScheduleStoreImpl(
    storeFactory: StoreFactory,
    params: StaffListForCompanyAndScheduleStore.Params,
    staffSource: StaffNetworkSource,
) : StaffListForCompanyAndScheduleStore,
    Store<StaffListForCompanyAndScheduleStore.Intent, StaffListForCompanyAndScheduleStore.State, StaffListForCompanyAndScheduleStore.Label> by storeFactory.create(
        name = "StaffListForCompanyAndScheduleStoreImpl",
        initialState = StaffListForCompanyAndScheduleStore.State(
            staff = emptyList(),
            isLoading = true,
            isFailure = true,
            schedule = null,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, staffSource) },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    message.staff.map { company ->
                        StaffListForCompanyAndScheduleStore.State.Staff(
                            id = company.id,
                            name = company.name,
                            codename = company.codename,
                            icon = company.icon,
                            role = company.role,
                        )
                    },
                    schedule = message.schedule,
                    isLoading = false,
                    isFailure = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: StaffListForCompanyAndScheduleStore.Params,
        private val staffSource: StaffNetworkSource,
    ) :
        SyncCoroutineExecutor<StaffListForCompanyAndScheduleStore.Intent, Action, StaffListForCompanyAndScheduleStore.State, Message, StaffListForCompanyAndScheduleStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> StaffListForCompanyAndScheduleStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadStaff(params.companyId, null)
            }
        }

        override fun executeIntent(
            intent: StaffListForCompanyAndScheduleStore.Intent,
            getState: () -> StaffListForCompanyAndScheduleStore.State,
        ) {
            when (intent) {
                is StaffListForCompanyAndScheduleStore.Intent.Refresh ->
                    loadStaff(params.companyId, getState().schedule)

                is StaffListForCompanyAndScheduleStore.Intent.RefreshSchedule ->
                    loadStaff(params.companyId, intent.schedule)
            }
        }

        var job: Job? = null

        private fun loadStaff(
            companyId: Long,
            schedule: LocalDateTime?,
        ) {
            dispatch(Message.Loading)
            val previousJob = job
            job = scope.launch {
                previousJob?.cancelAndJoin()
                if (schedule == null) {
                    syncDispatch(Message.Loaded(emptyList(), null))
                    return@launch
                }
                try {
                    val response =
                        staffSource.getStaffForCompanyAndSchedule(
                            GetStaffForCompanyAndScheduleInput(
                                companyId = companyId,
                                date = schedule
                            )
                        )
                    syncDispatch(
                        Message.Loaded(
                            response.staff.map { staff ->
                                Message.Loaded.Staff(
                                    id = staff.id,
                                    name = staff.name,
                                    codename = staff.codename,
                                    icon = staff.role,
                                    role = staff.role,
                                )
                            },
                            schedule,
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
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
            val staff: List<Staff>,
            val schedule: LocalDateTime?,
        ) : Message() {
            class Staff(
                val id: Long,
                val name: String,
                val codename: String,
                val icon: String?,
                val role: String,
            )
        }

    }
}