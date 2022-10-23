package ru.mclient.mvi.service.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.service.CreateServiceInput
import ru.mclient.network.service.ServiceNetworkSource

@Factory
class ServiceCreateStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam params: ServiceCreateStore.Params,
    serviceNetworkSource: ServiceNetworkSource,
) : ServiceCreateStore,
    Store<ServiceCreateStore.Intent, ServiceCreateStore.State, Nothing> by storeFactory.create(
        name = "ServiceCreateStoreImpl",
        initialState = ServiceCreateStore.State(
            title = "",
            description = "",
            cost = "0",
            isLoading = false,
            isError = false,
            isSuccess = false,
            service = null,
        ),
        executorFactory = {
            Executor(params = params, serviceNetworkSource = serviceNetworkSource)
        },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    title = message.title,
                    isError = true,
                    isLoading = false,
                    isSuccess = false,
                )

                is Message.Changed -> copy(
                    title = message.title,
                    description = message.description,
                    cost = message.cost,
                    isError = false, isLoading = false
                )

                is Message.Loading ->
                    copy(isLoading = true, isError = false, isSuccess = false)

                is Message.Success ->
                    copy(
                        isLoading = false,
                        isError = false,
                        isSuccess = true,
                        service = ServiceCreateStore.State.Service(
                            id = message.categoryId,
                            title = message.title,
                            cost = message.cost,
                            description = message.description,
                        )
                    )
            }
        }
    ) {

    class Executor(
        private val params: ServiceCreateStore.Params,
        private val serviceNetworkSource: ServiceNetworkSource,
    ) :
        SyncCoroutineExecutor<ServiceCreateStore.Intent, Nothing, ServiceCreateStore.State, Message, Nothing>() {

        override fun executeIntent(
            intent: ServiceCreateStore.Intent,
            getState: () -> ServiceCreateStore.State
        ) {
            when (intent) {
                is ServiceCreateStore.Intent.Create -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    dispatch(Message.Loading())
                    scope.launch {
                        val response = serviceNetworkSource.createService(
                            CreateServiceInput(
                                title = state.title,
                                companyId = params.companyId,
                                categoryId = params.categoryId,
                                description = state.description,
                                cost = state.cost,
                            )
                        )
                        syncDispatch(
                            Message.Success(
                                categoryId = response.id,
                                title = response.title,
                                cost = response.cost,
                                description = response.description,
                            )
                        )
                    }
                }

                is ServiceCreateStore.Intent.Update -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    val title = if (intent.title.length >= 64) state.title else intent.title
                    val description =
                        if (intent.description.length >= 1000) state.description else intent.description
                    val cost = intent.cost.filter(Char::isDigit)
                    dispatch(
                        Message.Changed(
                            title = title,
                            description = description,
                            cost = cost,
                        )
                    )
                }
            }
        }
    }


    sealed class Message {

        class Failed(
            val title: String,
            val description: String,
            val cost: String,
        ) : Message()

        class Changed(
            val title: String,
            val description: String,
            val cost: String,
        ) : Message()

        class Success(
            val categoryId: Long,
            val title: String,
            val description: String,
            val cost: String,
        ) : Message()

        class Loading : Message()

    }

}