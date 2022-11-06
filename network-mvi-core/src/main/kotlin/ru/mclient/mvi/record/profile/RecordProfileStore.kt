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
    }

    data class State(
        val record: Record?,
        val isFailure: Boolean,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) {

        class Record(
            val id: Long,
            val client: Client,
            val schedule: Schedule,
            val time: TimeOffset,
            val services: List<Service>,
            val totalCost: Long,
            val staff: Staff
        )

        class TimeOffset(
            val start: LocalTime,
            val end: LocalTime,
        )

        class Schedule(
            val id: Long,
            val date: LocalDate,
            val start: LocalTime,
            val end: LocalTime,
        )

        class Client(
            val id: Long,
            val name: String,
            val phone: String,
        )

        class Staff(
            val name: String,
            val role: String,
            val codename: String,
            val id: Long,
        )

        class Service(
            val id: Long,
            val title: String,
            val cost: Long,
            val description: String
        )


    }

    sealed class Label

}