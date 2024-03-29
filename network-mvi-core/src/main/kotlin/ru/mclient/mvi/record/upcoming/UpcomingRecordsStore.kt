package ru.mclient.mvi.record.upcoming

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate
import java.time.LocalTime

interface UpcomingRecordsStore :
    ParametrizedStore<UpcomingRecordsStore.Intent, UpcomingRecordsStore.State, UpcomingRecordsStore.Label, UpcomingRecordsStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val records: List<Record>,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) {
        class Record(
            val id: Long,
            val client: Client,
            val time: TimeOffset,
            val schedule: Schedule,
            val services: List<Service>,
        )

        class TimeOffset(
            val start: LocalTime,
            val end: LocalTime,
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
    }

    sealed class Label

}