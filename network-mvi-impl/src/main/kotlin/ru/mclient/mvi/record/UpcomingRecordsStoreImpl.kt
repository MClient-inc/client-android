package ru.mclient.mvi.record

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.record.GetRecordsForCompanyInput
import ru.mclient.network.record.RecordsNetworkSource
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class UpcomingRecordsStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    params: UpcomingRecordsStore.Params,
    private val recordsSource: RecordsNetworkSource,
) : UpcomingRecordsStore,
    Store<UpcomingRecordsStore.Intent, UpcomingRecordsStore.State, UpcomingRecordsStore.Label> by storeFactory.create(
        name = "UpcomingRecordsStoreImpl",
        initialState = UpcomingRecordsStore.State(
            records = emptyList(),
            isLoading = true,
            isFailure = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, recordsSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    records = message.records.map { record ->
                        UpcomingRecordsStore.State.Record(
                            id = record.id,
                            client = UpcomingRecordsStore.State.Client(
                                id = record.client.id,
                                name = record.client.name,
                                phone = record.client.phone,
                            ),
                            time = UpcomingRecordsStore.State.TimeOffset(
                                start = record.time.start,
                                end = record.time.end
                            ),
                            schedule = UpcomingRecordsStore.State.Schedule(
                                staff = UpcomingRecordsStore.State.Staff(
                                    id = record.schedule.staff.id,
                                    name = record.schedule.staff.name,
                                ),
                                date = record.schedule.date,
                            ),
                            services = record.services.map { service ->
                                UpcomingRecordsStore.State.Service(
                                    id = service.id,
                                    title = service.title
                                )
                            }
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
        private val params: UpcomingRecordsStore.Params,
        private val recordsSource: RecordsNetworkSource,
    ) :
        SyncCoroutineExecutor<UpcomingRecordsStore.Intent, Action, UpcomingRecordsStore.State, Message, UpcomingRecordsStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> UpcomingRecordsStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId)
            }
        }

        override fun executeIntent(
            intent: UpcomingRecordsStore.Intent,
            getState: () -> UpcomingRecordsStore.State,
        ) {
            when (intent) {
                is UpcomingRecordsStore.Intent.Refresh ->
                    loadCompanies(params.companyId)
            }
        }

        private fun loadCompanies(
            companyId: Long,
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val recordsResponse = recordsSource.getRecordsForCompany(
                        GetRecordsForCompanyInput(companyId)
                    )
                    syncDispatch(
                        Message.Loaded(
                            records = recordsResponse.records.map { record ->
                                Message.Loaded.Record(
                                    id = record.id,
                                    client = Message.Loaded.Client(
                                        id = record.client.id,
                                        name = record.client.name,
                                        phone = record.client.phone,
                                    ),
                                    schedule = Message.Loaded.Schedule(
                                        staff = Message.Loaded.Staff(
                                            id = record.schedule.staff.id,
                                            name = record.schedule.staff.name,
                                        ),
                                        date = record.schedule.date,
                                    ),
                                    services = record.services.map { service ->
                                        Message.Loaded.Service(
                                            id = service.id,
                                            title = service.title
                                        )
                                    },
                                    time = Message.Loaded.TimeOffset(
                                        start = record.time.start,
                                        end = record.time.end,
                                    )
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
            val records: List<Record>,
        ) : Message() {
            class Record(
                val id: Long,
                val client: Client,
                val schedule: Schedule,
                val time: TimeOffset,
                val services: List<Service>,
            )

            class Schedule(
                val staff: Staff,
                val date: LocalDate,
            )

            class Client(
                val id: Long,
                val name: String,
                val phone: String,
            )

            class Staff(
                val id: Long,
                val name: String,
            )

            class Service(
                val id: Long,
                val title: String,
            )

            class TimeOffset(
                val start: LocalTime,
                val end: LocalTime,
            )
        }

    }
}