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
import java.time.Month
import java.time.Month.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class RecordsListStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    params: RecordsListStore.Params,
    private val recordsSource: RecordsNetworkSource,
) : RecordsListStore,
    Store<RecordsListStore.Intent, RecordsListStore.State, RecordsListStore.Label> by storeFactory.create(
        name = "RecordsListStoreImpl",
        initialState = RecordsListStore.State(
            records = emptyList(),
            isLoading = true,
            isFailure = true,
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
                        RecordsListStore.State.Record(
                            id = record.id,
                            client = RecordsListStore.State.Client(
                                id = record.client.id,
                                name = record.client.name,
                                phone = record.client.phone,
                                formattedPhone = record.client.formattedPhone,
                            ),
                            time = RecordsListStore.State.TimeOffset(
                                start = record.time.start,
                                end = record.time.end
                            ),
                            schedule = RecordsListStore.State.Schedule(
                                staff = RecordsListStore.State.Staff(
                                    id = record.schedule.staff.id,
                                    name = record.schedule.staff.name,
                                ),
                                date = record.schedule.date,
                            ),
                            services = record.services.map { service ->
                                RecordsListStore.State.Service(
                                    id = service.id,
                                    title = service.title,
                                    cost = service.cost,
                                )
                            },
                            cost = record.cost,
                            formattedCost = record.formattedCost,
                            formattedTime = record.formattedTime,
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
        private val params: RecordsListStore.Params,
        private val recordsSource: RecordsNetworkSource,
    ) :
        SyncCoroutineExecutor<RecordsListStore.Intent, Action, RecordsListStore.State, Message, RecordsListStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> RecordsListStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId)
            }
        }

        override fun executeIntent(
            intent: RecordsListStore.Intent,
            getState: () -> RecordsListStore.State,
        ) {
            when (intent) {
                is RecordsListStore.Intent.Refresh ->
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
                        GetRecordsForCompanyInput(companyId, 200)
                    )
                    syncDispatch(
                        Message.Loaded(
                            records = recordsResponse.records.map { record ->
                                val cost = record.services.sumOf { it.cost }
                                Message.Loaded.Record(
                                    id = record.id,
                                    client = Message.Loaded.Client(
                                        id = record.client.id,
                                        name = record.client.name,
                                        phone = record.client.phone,
                                        formattedPhone = phoneNumberFormatter(record.client.phone),
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
                                            title = service.title,
                                            cost = service.cost,
                                        )
                                    },
                                    time = Message.Loaded.TimeOffset(
                                        start = record.time.start,
                                        end = record.time.end,
                                    ),
                                    formattedCost = "$cost ₽",
                                    formattedTime = "${record.schedule.date.format()} ${
                                        format(
                                            record.time.start,
                                            record.time.end
                                        )
                                    }",
                                    cost = cost,
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
                val formattedTime: String,
                val cost: Long,
                val formattedCost: String,
            )

            class Schedule(
                val staff: Staff,
                val date: LocalDate,
            )

            class Client(
                val id: Long,
                val name: String,
                val phone: String,
                val formattedPhone: String,
            )

            class Staff(
                val id: Long,
                val name: String,
            )

            class Service(
                val id: Long,
                val title: String,
                val cost: Long,
            )

            class TimeOffset(
                val start: LocalTime,
                val end: LocalTime,
            )
        }

    }
}

private val formatter = DateTimeFormatter.ofPattern("HH:mm")

private fun format(from: LocalTime, to: LocalTime): String {
    if (from == to) {
        return "c ${from.format()}"
    }
    return "c ${from.format()} до ${to.format()}"
}

private fun LocalTime.format(): String {
    return formatter.format(this)
}


private fun LocalDate.format(): String {
    val today = LocalDate.now()
    return when {
        this == today -> "Сегодня"
        this == today.plusDays(1) -> "Завтра"
        this == today.minusDays(1) -> "Вчера"
        this.year == today.year && this > today -> "${this.dayOfMonth} ${this.month.formatMonth()}"
        else -> "${this.dayOfMonth} ${this.month.formatMonth()} ${this.year}"
    }
}

private fun Month.formatMonth(): String {
    return when (this) {
        JANUARY -> "янв."
        FEBRUARY -> "фев."
        MARCH -> "мар."
        APRIL -> "апр."
        MAY -> "мая"
        JUNE -> "июн."
        JULY -> "июл."
        AUGUST -> "авг."
        SEPTEMBER -> "сен."
        OCTOBER -> "окт."
        NOVEMBER -> "ноя."
        DECEMBER -> "дек."
    }
}


fun phoneNumberFormatter(phoneNumber: String): String {
    if (phoneNumber.length != 11 && !phoneNumber.startsWith("7"))
        return phoneNumber
    return buildString {
        append(phoneNumber)
        insert(0, "+")
        insert(2, "(")
        insert(6, ") ")
        insert(10, "-")
        insert(13, "-")
    }
}
