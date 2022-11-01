package ru.mclient.common.record.upcoming

import java.time.LocalDate
import java.time.LocalTime

class UpcomingRecordsState(
    val records: List<Record>,
    val isLoading: Boolean,
    val isFailure: Boolean,
) {
    class Record(
        val id: Long,
        val client: Client,
        val schedule: Schedule,
        val time: TimeOffset,
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

interface UpcomingRecords {

    val state: UpcomingRecordsState

    fun onRefresh()

    fun onSelect(recordId: Long)

}