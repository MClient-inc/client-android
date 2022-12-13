package ru.mclient.mvi.servicecategory.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.servicecategory.CreateServiceCategoryInput
import ru.mclient.network.servicecategory.ServiceCategoryNetworkSource

@Factory
class ServiceCategoryCreateStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam params: ServiceCategoryCreateStore.Params,
    serviceCategoryNetworkSource: ServiceCategoryNetworkSource,
) : ServiceCategoryCreateStore,
    Store<ServiceCategoryCreateStore.Intent, ServiceCategoryCreateStore.State, Nothing> by storeFactory.create(
        name = "ServiceCategoryCreateStoreImpl",
        initialState = ServiceCategoryCreateStore.State(
            title = "",
            isLoading = false,
            isError = false,
            isSuccess = false,
            category = null,
        ),
        executorFactory = {
            Executor(params = params, serviceCategoryNetworkSource = serviceCategoryNetworkSource)
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
                    isError = false, isLoading = false
                )

                is Message.Loading ->
                    copy(isLoading = true, isError = false, title = message.title)

                is Message.Success ->
                    copy(
                        isLoading = false,
                        isError = false,
                        isSuccess = true,
                        category = ServiceCategoryCreateStore.State.ServiceCategory(
                            id = message.categoryId,
                            title = title,
                        )
                    )
            }
        }
    ) {

    class Executor(
        private val params: ServiceCategoryCreateStore.Params,
        private val serviceCategoryNetworkSource: ServiceCategoryNetworkSource,
    ) :
        SyncCoroutineExecutor<ServiceCategoryCreateStore.Intent, Nothing, ServiceCategoryCreateStore.State, Message, Nothing>() {

        override fun executeIntent(
            intent: ServiceCategoryCreateStore.Intent,
            getState: () -> ServiceCategoryCreateStore.State
        ) {
            when (intent) {
                is ServiceCategoryCreateStore.Intent.Create -> {
                    if (getState().isLoading)
                        return
                    dispatch(Message.Loading(title = intent.title))
                    scope.launch {
                        val response = serviceCategoryNetworkSource.createServiceCategory(
                            CreateServiceCategoryInput(
                                title = intent.title,
                                companyId = params.companyId.toString(),
                            )
                        )
                        syncDispatch(
                            Message.Success(
                                categoryId = response.id.toLong()
                            )
                        )
                    }
                }

                is ServiceCategoryCreateStore.Intent.Update -> {
                    val state = getState()
                    if (state.isLoading)
                        return
                    val title = if (intent.title.length >= 64) state.title else intent.title
                    dispatch(
                        Message.Changed(
                            title = title,
                        )
                    )
                }
            }
        }
    }


    sealed class Message {

        class Failed(val title: String) : Message()

        class Changed(val title: String) : Message()

        class Success(val categoryId: Long) : Message()

        class Loading(val title: String) : Message()

    }

}