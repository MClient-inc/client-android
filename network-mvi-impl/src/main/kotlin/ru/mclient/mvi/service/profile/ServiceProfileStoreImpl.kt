package ru.mclient.mvi.service.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.service.profile.ServiceProfileStore.*
import ru.mclient.mvi.service.profile.ServiceProfileStore.State.AnalyticsType.COMPANY
import ru.mclient.mvi.service.profile.ServiceProfileStore.State.AnalyticsType.NETWORK
import ru.mclient.network.service.GetServiceAnalyticsInput
import ru.mclient.network.service.GetServiceByIdInput
import ru.mclient.network.service.ServiceNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ServiceProfileStoreImpl(
    storeFactory: StoreFactory,
    params: Params,
    serviceSource: ServiceNetworkSource,
) : ServiceProfileStore,
    Store<Intent, State, Label> by storeFactory.create(
        name = "ServiceProfileStoreImpl",
        initialState = State(
            service = null,
            network = null,
            company = null,
            analyticsType = COMPANY,
            isTypeSelecting = false,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, serviceSource) },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(isFailure = true, isLoading = false)
                is Message.Loaded -> copy(
                    service = State.Service(
                        id = message.service.id,
                        title = message.service.title,
                        description = message.service.description,
                        cost = message.service.cost
                    ),
                    network = State.NetworkAnalytics(
                        message.network.id,
                        message.network.title,
                        message.network.analytics.toData()
                    ),
                    company = State.CompanyAnalytics(
                        message.company.id,
                        message.company.title,
                        message.company.analytics.toData(),
                    ),
                    isFailure = false,
                    isLoading = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true
                )

                Message.Dismiss -> copy(isTypeSelecting = false)
                Message.ToggleCompany -> copy(
                    isTypeSelecting = false,
                    analyticsType = COMPANY
                )

                Message.ToggleNetwork -> copy(
                    isTypeSelecting = false,
                    analyticsType = NETWORK,
                )

                Message.Select -> copy(isTypeSelecting = true)
            }
        }
    ) {

    class Executor(
        private val params: Params,
        private val serviceSource: ServiceNetworkSource,
    ) : SyncCoroutineExecutor<Intent, Action, State, Message, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                Action.FirstLoad -> loadService(params.serviceId, params.companyId)
            }
        }

        override fun executeIntent(
            intent: Intent,
            getState: () -> State,
        ) {
            when (intent) {
                Intent.Dismiss -> dispatch(Message.Dismiss)
                Intent.Refresh -> loadService(
                    params.serviceId,
                    params.companyId
                )

                Intent.ToggleCompany -> dispatch(Message.ToggleCompany)
                Intent.ToggleNetwork -> dispatch(Message.ToggleNetwork)
                Intent.Select -> dispatch(Message.Select)
            }
        }

        private fun loadService(serviceId: Long, companyId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response =
                        serviceSource.getServiceById(GetServiceByIdInput(serviceId.toString()))
                    val analytics = serviceSource.getServiceAnalytics(
                        GetServiceAnalyticsInput(
                            id = serviceId.toString(),
                            companyId = companyId.toString()
                        )
                    )
                    syncDispatch(
                        Message.Loaded(
                            service = Message.Loaded.Service(
                                id = response.id.toLong(),
                                title = response.title,
                                description = response.description,
                                cost = response.cost
                            ),
                            network = Message.Loaded.NetworkAnalytics(
                                id = analytics.network.id.toLong(),
                                title = analytics.network.title,
                                analytics = Message.Loaded.AnalyticsItem(
                                    comeCount = analytics.network.analytics.comeCount,
                                    notComeCount = analytics.network.analytics.notComeCount,
                                    waitingCount = analytics.network.analytics.waitingCount,
                                    totalRecords = analytics.network.analytics.totalRecords,
                                    popularity = analytics.network.analytics.popularity,
                                )
                            ),
                            company = Message.Loaded.CompanyAnalytics(
                                id = analytics.company.id.toLong(),
                                title = analytics.company.title,
                                analytics = Message.Loaded.AnalyticsItem(
                                    comeCount = analytics.company.analytics.comeCount,
                                    notComeCount = analytics.company.analytics.notComeCount,
                                    waitingCount = analytics.company.analytics.waitingCount,
                                    totalRecords = analytics.company.analytics.totalRecords,
                                    popularity = analytics.company.analytics.popularity,
                                )
                            ),
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed(e))
                }
            }
        }

    }

    sealed class Action {
        object FirstLoad : Action()
    }

    sealed class Message {

        object ToggleCompany : Message()

        object ToggleNetwork : Message()

        object Dismiss : Message()

        object Select : Message()

        data class Failed(val exception: Exception) : Message()
        object Loading : Message()
        class Loaded(
            val service: Service,
            val network: NetworkAnalytics,
            val company: CompanyAnalytics,
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
                val popularity: String,
            )

            class NetworkAnalytics(
                val id: Long,
                val title: String,
                val analytics: AnalyticsItem,
            )

            class CompanyAnalytics(
                val id: Long,
                val title: String,
                val analytics: AnalyticsItem,
            )
        }

    }
}

private fun ServiceProfileStoreImpl.Message.Loaded.AnalyticsItem.toData(): State.AnalyticsItem {
    return State.AnalyticsItem(
        comeCount = comeCount,
        notComeCount = notComeCount,
        waitingCount = waitingCount,
        totalRecords = totalRecords,
        popularity = popularity
    )
}
