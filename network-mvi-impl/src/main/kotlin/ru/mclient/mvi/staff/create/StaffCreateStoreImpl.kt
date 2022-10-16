package ru.mclient.mvi.staff.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor

@Factory
class StaffCreateStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam savedState: StaffCreateStore.State?,
    @InjectedParam params: StaffCreateStore.Param,
) : StaffCreateStore,
    Store<StaffCreateStore.Intent, StaffCreateStore.State, StaffCreateStore.Label> by storeFactory.create(
        name = "StaffCreateStoreImpl",
        initialState = savedState ?: StaffCreateStore.State(
            name = "",
            codename = "",
            description = "",
            isLoading = false,
            isError = false,
            isSuccess = false
        ),
        bootstrapper = coroutineBootstrapper {
            if (savedState != null && savedState.isLoading) {
                dispatch(
                    Action.RetryAction(
                        name = savedState.name,
                        codename = savedState.codename,
                        description = savedState.description
                    )
                )
            }
        },
        executorFactory = {
            Executor()
        },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    name = message.name,
                    codename = message.codename,
                    description = message.description,
                    isError = true, isLoading = false
                )

                is Message.Changed -> copy(
                    name = message.name,
                    codename = message.codename,
                    description = message.description,
                    isError = false, isLoading = false
                )

                is Message.Loading ->
                    copy(isLoading = true, isError = false)

                is Message.Success ->
                    copy(isLoading = false, isError = false, isSuccess = true)
            }
        }
    ) {

    class Executor :
        SyncCoroutineExecutor<StaffCreateStore.Intent, Action, StaffCreateStore.State, Message, Nothing>() {

        override fun executeAction(action: Action, getState: () -> StaffCreateStore.State) {
            when (action) {
                is Action.RetryAction -> onCreate(
                    name = action.name,
                    codename = action.codename,
                    description = action.description,
                )
            }

        }

        private fun onCreate(name: String, codename: String, description: String) {
            scope.launch {
                TODO()
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
                    description = intent.description
                )

                is StaffCreateStore.Intent.Update -> {
                    val state = getState()
                    val newCodename =
                        intent.codename.filterNot(Char::isWhitespace)
                    val codename = if (newCodename.length >= 32) state.codename else newCodename
                    val name = if (intent.name.length >= 64) state.name else intent.name
                    val description =
                        if (intent.description.length >= 1000) state.description else intent.description
                    dispatch(
                        Message.Changed(
                            name = name,
                            codename = codename,
                            description = description,
                        )
                    )
                }
            }
        }
    }

    sealed class Action {

        data class RetryAction(
            val name: String, val codename: String, val description: String
        ) : Action()

    }

    sealed class Message {

        class Failed(val name: String, val codename: String, val description: String) : Message()

        class Changed(val name: String, val codename: String, val description: String) : Message()

        class Success : Message()

        class Loading : Message()

    }

}