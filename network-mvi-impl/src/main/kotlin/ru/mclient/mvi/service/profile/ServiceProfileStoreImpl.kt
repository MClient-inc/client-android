package ru.mclient.mvi.service.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.service.GetServiceByIdInput
import ru.mclient.network.service.ServiceNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ServiceProfileStoreImpl(
    storeFactory: StoreFactory,
    params: ServiceProfileStore.Params,
    serviceSource: ServiceNetworkSource
) : ServiceProfileStore,
    Store<ServiceProfileStore.Intent, ServiceProfileStore.State, ServiceProfileStore.Label> by storeFactory.create(
        name = "ServiceProfileStoreImpl",
        initialState = ServiceProfileStore.State(
            service = null,
            network = null,
            company = null,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, serviceSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(isFailure = true, isLoading = false)
                is Message.Loaded -> copy(
                    ServiceProfileStore.State.Service(
                        id = message.service.id,
                        title = message.service.title,
                        description = message.service.description,
                        cost = message.service.cost
                    ),
                    isFailure = false,
                    isLoading = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true
                )
            }
        }
    ) {

    class Executor(
        private val params: ServiceProfileStore.Params,
        private val serviceSource: ServiceNetworkSource
    ) : SyncCoroutineExecutor<ServiceProfileStore.Intent, Action, ServiceProfileStore.State, Message, ServiceProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ServiceProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadService(params.serviceId)
            }
        }

        private fun loadService(serviceId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = serviceSource.getServiceById(GetServiceByIdInput(serviceId))
                    dispatch(
                        Message.Loaded(
                            service = Message.Loaded.Service(
                                id = response.id,
                                title = response.title,
                                description = response.description,
                                cost = response.cost
                            ),
                            network = TODO("network"),
                            company = TODO("company")
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
            val service: Service,
            val network: NetworkAnalytics,
            val company: CompanyAnalytics
        ) : Message() {
            class Service(
                val id: Long,
                val title: String,
                val description: String,
                val cost: String,
            )

            class AnalyticsItem(
                val comeCount: Long,
                val notComeCount: Long,
                val waitingCount: Long,
                val totalRecords: Long,
                val value: String
            )

            class NetworkAnalytics(
                val id: Long,
                val title: String,
                val analytics: AnalyticsItem
            )
            class CompanyAnalytics(
                val id: Long,
                val title: String,
                val analytics: AnalyticsItem
            )
        }

    }
}