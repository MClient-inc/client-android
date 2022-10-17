package ru.mclient.mvi.staff.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.staff.GetStaffForCompanyInput
import ru.mclient.network.staff.StaffNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class StaffListForCompanyStoreImpl(
    storeFactory: StoreFactory,
    params: StaffListForCompanyStore.Params,
    staffSource: StaffNetworkSource,
) : StaffListForCompanyStore,
    Store<StaffListForCompanyStore.Intent, StaffListForCompanyStore.State, StaffListForCompanyStore.Label> by storeFactory.create(
        name = "CompanyNetworksListForAccountStoreImpl",
        initialState = StaffListForCompanyStore.State(
            staff = emptyList(),
            isLoading = true,
            isFailure = true
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
                        StaffListForCompanyStore.State.Staff(
                            id = company.id,
                            name = company.name,
                            codename = company.codename,
                            icon = company.icon,
                            role = company.role,
                        )
                    },
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
        private val params: StaffListForCompanyStore.Params,
        private val staffSource: StaffNetworkSource,
    ) :
        SyncCoroutineExecutor<StaffListForCompanyStore.Intent, Action, StaffListForCompanyStore.State, Message, StaffListForCompanyStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> StaffListForCompanyStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadStaff(params.companyId)
            }
        }

        override fun executeIntent(
            intent: StaffListForCompanyStore.Intent,
            getState: () -> StaffListForCompanyStore.State
        ) {
            when (intent) {
                is StaffListForCompanyStore.Intent.Refresh ->
                    loadStaff(params.companyId)
            }
        }

        private fun loadStaff(
            companyId: Long
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response =
                        staffSource.getStaffForCompany(GetStaffForCompanyInput(companyId))
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
                            }
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