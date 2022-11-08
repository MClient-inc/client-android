package ru.mclient.network.record

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetRecordsForCompanyInput(val companyId: Long, val limit: Int = 6)

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
    val services: List<Long>,
)

class GetRecordByIdInput(
    val recordId: Long,
)

class GetRecordByIdOutput(
    val record: Record,
) {
    class Record(
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
    )
}

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

class EditRecordStatusInput(
    val recordId: Long,
    val status: RecordVisitStatus,
)

class PayWithAbonementsInput(
    val recordId: Long,
    val abonements: List<Long>,
)

class PayWithAbonementsOutput(
    val recordId: Long,
)

enum class RecordVisitStatus {
    WAITING, COME, NOT_COME,
}

class EditRecordStatusOutput(
    val recordId: Long,
    val status: RecordVisitStatus,
) {
    enum class RecordVisitStatus {
        WAITING, COME, NOT_COME,
    }
}