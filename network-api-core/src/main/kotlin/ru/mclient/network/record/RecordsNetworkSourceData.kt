package ru.mclient.network.record

import java.time.LocalDate
import java.time.LocalTime

class GetRecordsForCompanyInput(val companyId: Long)

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
        val from: LocalTime,
        val to: LocalTime,
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