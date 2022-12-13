package ru.mclient.mvi.client.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.CreateClientInput

@Factory
class ClientCreateStoreImpl(
    storeFactory: StoreFactory,
    params: ClientCreateStore.Params,
    state: ClientCreateStore.State?,
    clientNetworkSource: ClientNetworkSource,
) : ClientCreateStore,
    Store<ClientCreateStore.Intent, ClientCreateStore.State, Nothing> by storeFactory.create(
        name = "ClientCreateStoreImpl",
        initialState = state ?: ClientCreateStore.State(
            name = "",
            phone = "7",
            isLoading = false,
            isError = false,
            isSuccess = false,
            client = null,
        ),
        executorFactory = {
            Executor(params = params, clientNetworkSource = clientNetworkSource)
        },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    name = message.name,
                    phone = message.phone,
                    isError = true,
                    isLoading = false,
                    isSuccess = false,
                )

                is Message.Changed -> copy(
                    name = message.name,
                    phone = message.phone,
                    isError = false, isLoading = false
                )

                is Message.Loading ->
                    copy(isLoading = true, isError = false, isSuccess = false)

                is Message.Success ->
                    copy(
                        isLoading = false,
                        isError = false,
                        isSuccess = true,
                        client = ClientCreateStore.State.Client(
                            id = message.clientId,
                            name = message.name,
                            phone = message.phone,
                        )
                    )
            }
        }
    ) {

    class Executor(
        private val params: ClientCreateStore.Params,
        private val clientNetworkSource: ClientNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientCreateStore.Intent, Nothing, ClientCreateStore.State, Message, Nothing>() {

        override fun executeIntent(
            intent: ClientCreateStore.Intent,
            getState: () -> ClientCreateStore.State,
        ) {
            when (intent) {
                is ClientCreateStore.Intent.Create -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    dispatch(Message.Loading())
                    scope.launch {
                        val response = clientNetworkSource.createClient(
                            CreateClientInput(
                                companyId = params.companyId.toString(),
                                name = state.name,
                                phone = state.phone,
                            )
                        )
                        syncDispatch(
                            Message.Success(
                                clientId = response.id.toLong(),
                                name = response.name,
                                phone = response.phone.orEmpty(),
                            )
                        )
                    }
                }

                is ClientCreateStore.Intent.Update -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    val name = if (intent.name.length >= 64) state.name else intent.name
                    val phone =
                        if (intent.phone != state.phone) intent.phone.formatAsRussianNumber() else state.phone
                    dispatch(
                        Message.Changed(
                            name = name,
                            phone = phone,
                        )
                    )
                }
            }
        }
    }


    sealed class Message {

        class Failed(
            val name: String,
            val phone: String,
        ) : Message()

        class Changed(
            val name: String,
            val phone: String,
        ) : Message()

        class Success(
            val clientId: Long,
            val name: String,
            val phone: String,
        ) : Message()

        class Loading : Message()

    }

}

private fun String.filterDigits(): String {
    return filter(Char::isDigit).trim()
}

fun String.formatAsRussianNumber(): String {
    var phone = filterDigits()
    if (phone.firstOrNull() != '7') phone = "7$phone"
    return phone.sliceIgnoreLast(0, 11)
}

fun String.toPhoneFormat(): String {
    val input = filterDigits()
    if (!this.startsWith("7") || length > 11)
        return this
    return buildString {
        append(this@toPhoneFormat)
        insert(0, "+")
        if (input.length > 1)
            insert(2, " (")
        if (input.length >= 5)
            insert(7, ") ")
        if (input.length >= 8)
            insert(12, "-")
        if (input.length >= 10)
            insert(15, "-")
    }
}


private fun String.sliceIgnoreLast(start: Int, end: Int = length): String {
    return if (end > length) {
        substring(start, length)
    } else {
        substring(start, end)
    }
}