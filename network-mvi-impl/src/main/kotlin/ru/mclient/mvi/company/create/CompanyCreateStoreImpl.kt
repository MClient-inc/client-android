package ru.mclient.mvi.company.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor

@Factory
class CompanyCreateStoreImpl(
    storeFactory: StoreFactory,
) : CompanyCreateStore,
    Store<CompanyCreateStore.Intent, CompanyCreateStore.State, Nothing> by storeFactory.create(
        name = "CompanyCreateStoreImpl",
        initialState = CompanyCreateStore.State(
            title = "",
            codename = "",
            description = "",
            isLoading = false,
            isError = false,
            isSuccess = false
        ),
        executorFactory = {
            Executor()
        },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    title = message.title,
                    codename = message.codename,
                    description = message.description,
                    isError = true, isLoading = false
                )

                is Message.Changed -> copy(
                    title = message.title,
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
        SyncCoroutineExecutor<CompanyCreateStore.Intent, Nothing, CompanyCreateStore.State, Message, Nothing>() {

        override fun executeIntent(
            intent: CompanyCreateStore.Intent,
            getState: () -> CompanyCreateStore.State
        ) {
            when (intent) {
                is CompanyCreateStore.Intent.Create -> {
                    scope.launch {
                        TODO()
                    }
                }

                is CompanyCreateStore.Intent.Update -> {
                    val state = getState()
                    val newCodename =
                        intent.codename.filterNot(Char::isWhitespace)
                    val codename = if (newCodename.length >= 32) state.codename else newCodename
                    val title = if (intent.title.length >= 64) state.title else intent.title
                    val description =
                        if (intent.description.length >= 1000) state.description else intent.description
                    dispatch(
                        Message.Changed(
                            title = title,
                            codename = codename,
                            description = description,
                        )
                    )
                }
            }
        }
    }


    sealed class Message {

        class Failed(val title: String, val codename: String, val description: String) : Message()

        class Changed(val title: String, val codename: String, val description: String) : Message()

        class Success : Message()

        class Loading : Message()

    }

}