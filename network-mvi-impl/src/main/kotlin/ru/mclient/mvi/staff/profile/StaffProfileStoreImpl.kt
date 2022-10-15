package ru.mclient.mvi.staff.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.staff.list.StaffProfileStore
import ru.mclient.network.staff.GetStaffByIdInput
import ru.mclient.network.staff.StaffNetworkSource

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
            isFailure = false,
            isLoading = true
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
                    StaffProfileStore.State.Staff(
                        id = message.staff.id,
                        name = message.staff.name,
                        codename = message.staff.codename,
                    ),
                    isFailure = false,
                    isLoading = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
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
            getState: () -> StaffProfileStore.State
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
                    dispatch(
                        Message.Loaded(
                            Message.Loaded.Staff(
                                id = response.id,
                                name = response.name,
                                codename = response.codename,
                            )
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
            val staff: Staff
        ) : Message() {
            class Staff(
                val id: Long,
                val name: String,
                val codename: String,
            )
        }

    }
}