package ru.mclient.mvi.staff.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.staff.CreateStaffInput
import ru.mclient.network.staff.StaffNetworkSource

@Factory
class StaffCreateStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam savedState: StaffCreateStore.State?,
    @InjectedParam params: StaffCreateStore.Param,
    staffNetworkSource: StaffNetworkSource,
    coroutineDispatcher: CoroutineDispatcher,
) : StaffCreateStore,
    Store<StaffCreateStore.Intent, StaffCreateStore.State, StaffCreateStore.Label> by storeFactory.create(
        name = "StaffCreateStoreImpl",
        initialState = savedState ?: StaffCreateStore.State(
            name = "",
            codename = "",
            role = "",
            isLoading = false,
            isError = false,
            createdStaff = null,
            isButtonEnabled = false,
        ),
        bootstrapper = coroutineBootstrapper {
            if (savedState != null && savedState.isLoading) {
                dispatch(
                    Action.RetryAction(
                        name = savedState.name,
                        codename = savedState.codename,
                        role = savedState.role
                    )
                )
            }
        },
        executorFactory = {
            Executor(
                params = params,
                staffNetworkSource = staffNetworkSource,
                coroutineDispatcher = coroutineDispatcher,
            )
        },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    name = message.name,
                    codename = message.codename,
                    role = message.role,
                    isError = true,
                    isLoading = false
                )

                is Message.Changed -> copy(
                    name = message.name,
                    codename = message.codename,
                    role = message.role,
                    isError = false,
                    isLoading = false,
                    isButtonEnabled = message.isButtonEnabled,
                )

                is Message.Loading ->
                    copy(isLoading = true, isError = false)

                is Message.Success ->
                    copy(
                        name = message.name,
                        codename = message.codename,
                        role = message.role,
                        isLoading = false,
                        isError = false,
                        isButtonEnabled = false,
                        createdStaff = StaffCreateStore.State.Staff(
                            id = message.staffId,
                            name = message.name,
                            codename = message.codename,
                            role = message.role,
                        )
                    )
            }
        }
    ) {

    class Executor(
        private val params: StaffCreateStore.Param,
        private val staffNetworkSource: StaffNetworkSource,
        coroutineDispatcher: CoroutineDispatcher,
    ) :
        SyncCoroutineExecutor<StaffCreateStore.Intent, Action, StaffCreateStore.State, Message, Nothing>(
            coroutineDispatcher
        ) {

        override fun executeAction(action: Action, getState: () -> StaffCreateStore.State) {
            when (action) {
                is Action.RetryAction -> onCreate(
                    name = action.name,
                    codename = action.codename,
                    role = action.role,
                )
            }

        }

        private fun onCreate(name: String, codename: String, role: String) {
            dispatch(Message.Loading())
            scope.launch {
                val response = staffNetworkSource.createStaff(
                    CreateStaffInput(
                        companyId = params.companyId,
                        name = name,
                        codename = codename,
                        role = role
                    )
                )
                dispatch(
                    Message.Success(
                        staffId = response.id,
                        codename = response.codename,
                        name = response.name,
                        role = response.role,
                    )
                )
            }
        }

        override fun executeIntent(
            intent: StaffCreateStore.Intent,
            getState: () -> StaffCreateStore.State
        ) {
            when (intent) {
                is StaffCreateStore.Intent.Create -> onCreate(
                    name = intent.name,
                    codename = intent.codename,
                    role = intent.role
                )

                is StaffCreateStore.Intent.Update -> {
                    val state = getState()
                    val newCodename =
                        intent.codename.filterNot(Char::isWhitespace)
                    val codename = if (newCodename.length >= 32) state.codename else newCodename
                    val name = if (intent.name.length >= 64) state.name else intent.name
                    val role =
                        if (intent.role.length >= 100) state.role else intent.role
                    val isButtonEnabled = name.length >= 2 && codename.length >= 6
                    dispatch(
                        Message.Changed(
                            name = name,
                            codename = codename,
                            role = role,
                            isButtonEnabled = isButtonEnabled,
                        )
                    )
                }
            }
        }
    }

    sealed class Action {

        data class RetryAction(
            val name: String, val codename: String, val role: String
        ) : Action()

    }

    sealed class Message {

        class Failed(val name: String, val codename: String, val role: String) : Message()

        class Changed(
            val name: String,
            val codename: String,
            val role: String,
            val isButtonEnabled: Boolean,
        ) : Message()

        class Success(
            val staffId: Long,
            val name: String,
            val codename: String,
            val role: String,
        ) : Message()

        class Loading : Message()

    }

}