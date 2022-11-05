package ru.mclient.network.record

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetRecordsForCompanyInput(val companyId: Long, val limit: Int = 5)

class GetRecordsForCompanyOutput(
    val records: List<Record>,
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
        val start: LocalTime,
        val end: LocalTime,
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
        val cost: Long,
    )
}

class CreateRecordInput(
    val companyId: Long,
    val clientId: Long,
    val staffId: Long,
    val dateTime: LocalDateTime,
)

class CreateRecordOutput(
    val record: Record,
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
        val start: LocalTime,
        val end: LocalTime,
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
        val cost: Long,
    )
}