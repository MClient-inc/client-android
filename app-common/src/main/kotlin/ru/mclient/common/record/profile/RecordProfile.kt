package ru.mclient.common.record.profile

import java.time.LocalDate
import java.time.LocalTime

data class RecordProfileState(
    val record: Record?,
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
        val staff: Staff
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

    enum class RecordStatus {
        NOT_COME,
        COME,
        WAITING,
    }

}


interface RecordProfile {

    val state: RecordProfileState

    fun onRefresh()

}