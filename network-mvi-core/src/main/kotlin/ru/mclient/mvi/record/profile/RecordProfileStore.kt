package ru.mclient.mvi.record.profile

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate
import java.time.LocalTime


interface RecordProfileStore :
    ParametrizedStore<RecordProfileStore.Intent, RecordProfileStore.State, RecordProfileStore.Label, RecordProfileStore.Params> {

    @JvmInline
    value class Params(
        val recordId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()

        object Waiting : Intent()

        object Come : Intent()

        object NotCome : Intent()

        data class UseAbonements(val abonementIds: List<Long>) : Intent()

    }

    data class State(
        val record: Record?,
        val isFailure: Boolean,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) {

        data class Record(
            val id: Long,
            val client: Client,
            val schedule: Schedule,
            val time: TimeOffset,
            val services: List<Service>,
            val totalCost: Long,
            val staff: Staff,
            val status: RecordVisitStatus,
            val abonements: List<ClientAbonement>,
        )

        class ClientAbonement(
            val id: Long,
            val abonement: Abonement,
            val usages: Int,
        )

        data class Abonement(
            val id: Long,
            val title: String,
            val subabonement: Subabonement,
        )

        data class Subabonement(
            val id: Long,
            val title: String,
            val maxUsages: Int,
            val cost: Long,
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


        enum class RecordVisitStatus {
            WAITING, COME, NOT_COME,
        }

    }

    sealed class Label

}