package ru.mclient.mvi.record.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.record.EditRecordStatusInput
import ru.mclient.network.record.GetRecordByIdInput
import ru.mclient.network.record.RecordVisitStatus
import ru.mclient.network.record.RecordVisitStatus.*
import ru.mclient.network.record.RecordsNetworkSource
import java.time.LocalDate
import java.time.LocalTime


@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class RecordProfileStoreImpl(
    storeFactory: StoreFactory,
    params: RecordProfileStore.Params,
    recordSource: RecordsNetworkSource,
) : RecordProfileStore,
    Store<RecordProfileStore.Intent, RecordProfileStore.State, RecordProfileStore.Label> by storeFactory.create(
        name = "RecordProfileStoreImpl",
        initialState = RecordProfileStore.State(
            null,
            isFailure = false,
            isLoading = true,
            isRefreshing = false,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, recordSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    record = RecordProfileStore.State.Record(
                        id = message.record.id,
                        client = RecordProfileStore.State.Client(
                            id = message.record.client.id,
                            name = message.record.client.name,
                            phone = message.record.client.phone
                        ),
                        schedule = RecordProfileStore.State.Schedule(
                            id = message.record.schedule.id,
                            date = message.record.schedule.date,
                            start = message.record.schedule.start,
                            end = message.record.schedule.end,
                        ),
                        time = RecordProfileStore.State.TimeOffset(
                            start = message.record.time.start,
                            end = message.record.time.end
                        ),
                        staff = RecordProfileStore.State.Staff(
                            id = message.record.staff.id,
                            role = message.record.staff.role,
                            name = message.record.staff.name,
                            codename = message.record.staff.codename
                        ),
                        services = message.record.services.map { s ->
                            RecordProfileStore.State.Service(
                                id = s.id,
                                cost = s.cost,
                                title = s.title,
                            )
                        },
                        totalCost = message.record.totalCost,
                        status = when (message.record.status) {
                            Message.RecordVisitStatus.WAITING -> RecordProfileStore.State.RecordVisitStatus.WAITING
                            Message.RecordVisitStatus.COME -> RecordProfileStore.State.RecordVisitStatus.COME
                            Message.RecordVisitStatus.NOT_COME -> RecordProfileStore.State.RecordVisitStatus.NOT_COME
                        }
                    ),
                    isFailure = false,
                    isLoading = false,
                    isRefreshing = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                    isRefreshing = record != null,
                )

                is Message.UpdateStatus -> copy(
                    record = record?.let {
                        it.copy(
                            status = when (message.status) {
                                Message.RecordVisitStatus.WAITING -> RecordProfileStore.State.RecordVisitStatus.WAITING
                                Message.RecordVisitStatus.COME -> RecordProfileStore.State.RecordVisitStatus.COME
                                Message.RecordVisitStatus.NOT_COME -> RecordProfileStore.State.RecordVisitStatus.NOT_COME
                            }
                        )
                    }
                )
            }
        }
    ) {

    class Executor(
        private val params: RecordProfileStore.Params,
        private val recordSource: RecordsNetworkSource,
    ) :
        SyncCoroutineExecutor<RecordProfileStore.Intent, Action, RecordProfileStore.State, Message, RecordProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> RecordProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadRecord(params.recordId)
            }
        }

        override fun executeIntent(
            intent: RecordProfileStore.Intent,
            getState: () -> RecordProfileStore.State,
        ) {
            when (intent) {
                RecordProfileStore.Intent.Refresh -> loadRecord(params.recordId)
                RecordProfileStore.Intent.Come -> editRecordStatus(
                    recordId = params.recordId,
                    status = Message.RecordVisitStatus.COME,
                    getState = getState,
                )

                RecordProfileStore.Intent.NotCome -> editRecordStatus(
                    recordId = params.recordId,
                    status = Message.RecordVisitStatus.NOT_COME,
                    getState = getState,
                )

                RecordProfileStore.Intent.Waiting -> editRecordStatus(
                    recordId = params.recordId,
                    status = Message.RecordVisitStatus.WAITING,
                    getState = getState,
                )
            }
        }

        private fun editRecordStatus(
            recordId: Long,
            status: Message.RecordVisitStatus,
            getState: () -> RecordProfileStore.State,
        ) {
            val state = getState()
            val record = state.record
            if (state.isLoading || record == null)
                return
            dispatch(Message.UpdateStatus(status))
            scope.launch {
                try {
                    recordSource.editRecordStatus(
                        EditRecordStatusInput(
                            recordId = recordId,
                            status = when (status) {
                                Message.RecordVisitStatus.WAITING -> WAITING
                                Message.RecordVisitStatus.COME -> COME
                                Message.RecordVisitStatus.NOT_COME -> NOT_COME
                            }
                        )
                    )
                } catch (e: Exception) {
                    dispatch(
                        Message.UpdateStatus(
                            when (record.status) {
                                RecordProfileStore.State.RecordVisitStatus.WAITING -> Message.RecordVisitStatus.WAITING
                                RecordProfileStore.State.RecordVisitStatus.COME -> Message.RecordVisitStatus.COME
                                RecordProfileStore.State.RecordVisitStatus.NOT_COME -> Message.RecordVisitStatus.NOT_COME
                            }
                        )
                    )
                }
            }
        }

        private fun loadRecord(recordId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = recordSource.getRecordById(GetRecordByIdInput(recordId))
                    dispatch(
                        Message.Loaded(
                            record = Message.Loaded.Record(
                                id = response.record.id,
                                client = Message.Loaded.Client(
                                    id = response.record.client.id,
                                    name = response.record.client.name,
                                    phone = response.record.client.phone
                                ),
                                time = Message.Loaded.TimeOffset(
                                    start = response.record.time.start,
                                    end = response.record.time.end
                                ),
                                schedule = Message.Loaded.Schedule(
                                    id = response.record.schedule.id,
                                    start = response.record.schedule.start,
                                    end = response.record.schedule.end,
                                    date = response.record.schedule.date
                                ),
                                totalCost = response.record.totalCost,
                                staff = Message.Loaded.Staff(
                                    id = response.record.staff.id,
                                    codename = response.record.staff.codename,
                                    name = response.record.staff.name,
                                    role = response.record.staff.role
                                ),
                                services = response.record.services.map { s ->
                                    Message.Loaded.Service(
                                        id = s.id,
                                        cost = s.cost,
                                        title = s.title,
                                    )
                                },
                                status = when (response.record.status) {
                                    WAITING -> Message.RecordVisitStatus.WAITING
                                    COME -> Message.RecordVisitStatus.COME
                                    NOT_COME -> Message.RecordVisitStatus.NOT_COME
                                }
                            )
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

        data class UpdateStatus(val status: RecordVisitStatus) : Message()

        data class Loaded(
            val record: Record,
        ) : Message() {
            data class Record(
                val id: Long,
                val client: Client,
                val schedule: Schedule,
                val time: TimeOffset,
                val services: List<Service>,
                val totalCost: Long,
                val staff: Staff,
                val status: RecordVisitStatus,
            )

            data class TimeOffset(
                val start: LocalTime,
                val end: LocalTime,
            )

            data class Schedule(
                val id: Long,
                val date: LocalDate,
                val start: LocalTime,
                val end: LocalTime,
            )

            data class Client(
                val id: Long,
                val name: String,
                val phone: String,
            )

            data class Staff(
                val name: String,
                val role: String,
                val codename: String,
                val id: Long,
            )

            data class Service(
                val id: Long,
                val title: String,
                val cost: Long,
            )


        }


        enum class RecordVisitStatus {
            WAITING, COME, NOT_COME,
        }

    }
}