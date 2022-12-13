package ru.mclient.mvi.client.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.client.profile.ClientProfileStoreImpl.Message.Loaded.RecordStatus.*
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.GetAbonementsForClientInput
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.GetClientAnalyticsInput
import ru.mclient.network.client.GetClientAnalyticsOutput
import ru.mclient.network.client.GetClientByIdInput
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ClientProfileStoreImpl(
    storeFactory: StoreFactory,
    params: ClientProfileStore.Params,
    clientSource: ClientNetworkSource,
    abonementSource: AbonementNetworkSource,
) : ClientProfileStore,
    Store<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label> by storeFactory.create(
        name = "ClientProfileStoreImpl",
        initialState = ClientProfileStore.State(
            client = null,
            abonements = null,
            records = null,
            networkAnalytics = null,
            companyAnalytics = null,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, clientSource, abonementSource) },
        reducer = { message ->
            when (message) {
                is Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    client = ClientProfileStore.State.Client(
                        id = message.client.id,
                        name = message.client.name,
                        phone = message.client.phone
                    ),
                    abonements = message.abonements.map {
                        ClientProfileStore.State.ClientAbonement(
                            id = it.id,
                            usages = it.usages,
                            abonement = ClientProfileStore.State.Abonement(
                                title = it.abonement.title,
                                subabonement = ClientProfileStore.State.Subabonement(
                                    title = it.abonement.subabonement.title,
                                    maxUsages = it.abonement.subabonement.maxUsages
                                )
                            )
                        )
                    },
                    companyAnalytics = message.companyAnalytics?.let {
                        ClientProfileStore.State.CompanyAnalytics(
                            id = it.id.toLong(),
                            title = it.title,
                            analytics = ClientProfileStore.State.ClientAnalyticsItem(
                                notComeCount = it.analytics.notComeCount,
                                comeCount = it.analytics.comeCount,
                                waitingCount = it.analytics.waitingCount,
                                totalCount = it.analytics.totalCount,
                            )
                        )
                    },
                    networkAnalytics = message.networkAnalytics.let {
                        ClientProfileStore.State.NetworkAnalytics(
                            id = it.id.toLong(),
                            title = it.title,
                            analytics = ClientProfileStore.State.ClientAnalyticsItem(
                                notComeCount = it.analytics.notComeCount,
                                comeCount = it.analytics.comeCount,
                                waitingCount = it.analytics.waitingCount,
                                totalCount = it.analytics.totalCount,
                            )
                        )
                    },
                    records = message.records.map { record ->
                        ClientProfileStore.State.Record(
                            id = record.id.toLong(),
                            company = ClientProfileStore.State.Company(
                                record.company.id.toLong(),
                                record.company.title,
                            ),
                            time = ClientProfileStore.State.Time(
                                record.time.date,
                                record.time.start,
                                record.time.end,
                            ),
                            staff = ClientProfileStore.State.Staff(
                                record.staff.id.toLong(),
                                record.staff.name,
                            ),
                            services = record.services.map { saervice ->
                                ClientProfileStore.State.Service(
                                    saervice.id.toLong(),
                                    saervice.title,
                                    saervice.cost,
                                )
                            },
                            totalCost = record.totalCost,
                            status = when (record.status) {
                                WAITING -> ClientProfileStore.State.RecordStatus.WAITING
                                COME -> ClientProfileStore.State.RecordStatus.COME
                                NOT_COME -> ClientProfileStore.State.RecordStatus.NOT_COME
                            }
                        )
                    },
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
        private val params: ClientProfileStore.Params,
        private val clientSource: ClientNetworkSource,
        private val abonementSource: AbonementNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientProfileStore.Intent, Action, ClientProfileStore.State, Message, ClientProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ClientProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadClient(params.clientId, params.companyId)
            }
        }

        override fun executeIntent(
            intent: ClientProfileStore.Intent,
            getState: () -> ClientProfileStore.State,
        ) {
            when (intent) {
                ClientProfileStore.Intent.Refresh -> loadClient(
                    params.clientId,
                    params.companyId,
                )
            }
        }

        private fun loadClient(clientId: Long, companyId: Long?) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response =
                        clientSource.getClientById(GetClientByIdInput(clientId.toString()))
                    val abonements = abonementSource.getAbonementsForClient(
                        GetAbonementsForClientInput((clientId.toString()))
                    )
                    val analytics = clientSource.getClientAnalytics(
                        GetClientAnalyticsInput(
                            clientId = clientId.toString(),
                            companyId = companyId?.toString(),
                        )
                    )
                    dispatch(
                        Message.Loaded(
                            Message.Loaded.Client(
                                id = response.id.toLong(),
                                name = response.name,
                                phone = response.phone
                            ),
                            abonements.abonements.map {
                                Message.Loaded.ClientAbonement(
                                    id = it.id.toLong(),
                                    usages = it.usages,
                                    abonement = Message.Loaded.Abonement(
                                        title = it.abonement.title,
                                        subabonement = Message.Loaded.Subabonement(
                                            title = it.abonement.subabonement.title,
                                            maxUsages = it.abonement.subabonement.maxUsages,
                                        )
                                    ),
                                )
                            },
                            companyAnalytics = analytics.company?.let {
                                Message.Loaded.CompanyAnalytics(
                                    id = it.id.toLong(),
                                    title = it.title,
                                    analytics = Message.Loaded.ClientAnalyticsItem(
                                        notComeCount = it.analytics.notComeCount,
                                        comeCount = it.analytics.comeCount,
                                        waitingCount = it.analytics.waitingCount,
                                        totalCount = it.analytics.totalCount,
                                    )
                                )
                            },
                            networkAnalytics = analytics.network.let {
                                Message.Loaded.NetworkAnalytics(
                                    id = it.id.toLong(),
                                    title = it.title,
                                    analytics = Message.Loaded.ClientAnalyticsItem(
                                        notComeCount = it.analytics.notComeCount,
                                        comeCount = it.analytics.comeCount,
                                        waitingCount = it.analytics.waitingCount,
                                        totalCount = it.analytics.totalCount,
                                    )
                                )
                            },
                            records = analytics.upcomingRecords.map { record ->
                                Message.Loaded.Record(
                                    id = record.id.toLong(),
                                    company = Message.Loaded.Company(
                                        record.company.id.toLong(),
                                        record.company.title,
                                    ),
                                    time = Message.Loaded.Time(
                                        record.time.date,
                                        record.time.start,
                                        record.time.end,
                                    ),
                                    staff = Message.Loaded.Staff(
                                        record.staff.id.toLong(),
                                        record.staff.name,
                                    ),
                                    services = record.services.map { service ->
                                        Message.Loaded.Service(
                                            service.id.toLong(),
                                            service.title,
                                            service.cost,
                                        )
                                    },
                                    totalCost = record.totalCost,
                                    status = when (record.status) {
                                        GetClientAnalyticsOutput.RecordStatus.WAITING -> Message.Loaded.RecordStatus.WAITING
                                        GetClientAnalyticsOutput.RecordStatus.COME -> Message.Loaded.RecordStatus.COME
                                        GetClientAnalyticsOutput.RecordStatus.NOT_COME -> Message.Loaded.RecordStatus.NOT_COME
                                    }
                                )
                            }
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
        data class Failed(val exception: Exception) : Message()
        object Loading : Message()
        class Loaded(
            val client: Client,
            val abonements: List<ClientAbonement>,
            val networkAnalytics: NetworkAnalytics,
            val companyAnalytics: CompanyAnalytics?,
            val records: List<Record>,
        ) : Message() {
            class Client(
                val id: Long,
                val name: String,
                val phone: String,
            )

            class ClientAbonement(
                val id: Long,
                val usages: Int,
                val abonement: Abonement,
            )

            class Abonement(
                val title: String,
                val subabonement: Subabonement,
            )

            class Subabonement(
                val title: String,
                val maxUsages: Int,
            )

            //    Analytics

            class ClientAnalyticsItem(
                var notComeCount: Long,
                var comeCount: Long,
                var waitingCount: Long,
                val totalCount: Long,
            )

            class NetworkAnalytics(
                val id: Long,
                val title: String,
                val analytics: ClientAnalyticsItem,
            )

            class CompanyAnalytics(
                val id: Long,
                val title: String,
                val analytics: ClientAnalyticsItem,
            )

            class Record(
                val id: Long,
                val company: Company,
                val time: Time,
                val staff: Staff,
                val services: List<Service>,
                val totalCost: Long,
                val status: RecordStatus,
            )

            enum class RecordStatus {
                WAITING, COME, NOT_COME,
            }

            class Service(
                val id: Long,
                val title: String,
                val cost: Long,
            )

            class Staff(
                val id: Long,
                val name: String,
            )

            class Time(
                val date: LocalDate,
                val start: LocalTime,
                val end: LocalTime,
            )

            class Company(
                val id: Long,
                val title: String,
            )

        }

    }
}