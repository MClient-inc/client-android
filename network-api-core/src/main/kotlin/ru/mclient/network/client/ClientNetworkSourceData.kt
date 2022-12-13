package ru.mclient.network.client

import java.time.LocalDate
import java.time.LocalTime

class GetClientsForCompanyInput(val companyId: String)
class GetClientsForCompanyOutput(
    val clients: List<Client>,
) {
    class Client(
        val id: String,
        val title: String,
        val phone: String,
    )
}

data class GetClientByIdInput(
    val clientId: String,
)

data class GetClientByIdOutput(
    val id: String,
    val name: String,
    val phone: String,
)

class CreateClientInput(
    val companyId: String,
    val name: String,
    val phone: String,
)

class CreateClientOutput(
    val id: String,
    val name: String,
    val phone: String,
)

data class GetClientCardInput(val clientId: String)

data class GetClientCardOutput(val code: String)

data class GetClientAnalyticsInput(
    val clientId: String,
    val companyId: String?,
)

data class GetClientAnalyticsOutput(
    val upcomingRecords: List<Record>,
    val network: NetworkAnalytics,
    val company: CompanyAnalytics?,
) {
    class NetworkAnalytics(
        val id: String,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    class CompanyAnalytics(
        val id: String,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )
    class ClientAnalyticsItem(
        val notComeCount: Long,
        val comeCount: Long,
        val waitingCount: Long,
        val totalCount: Long,
    )

    class Record(
        val id: String,
        val company: Company,
        val time: Time,
        val staff: Staff,
        val services: List<Service>,
        val totalCost: Long,
        val status: RecordStatus,
    )

    enum class RecordStatus {
        WAITING, COME, NOT_COME,
    }

    class Time(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )

    class Service(
        val id: String,
        val title: String,
        val cost: Long,
    )
    class Staff(
        val id: String,
        val name: String,
    )
    class Company(
        val id: String,
        val title: String,
    )
}
//    class Record(
//        val id: String,
//        val company: Company,
//        val time: Time,
//        val staff: Staff,
//        val services: List<Service>,
//        val totalCost: Long
//    )
//    class ClientAnalyticsItem(
//        var notComeCount: Long,
//        var comeCount: Long,
//        var waitingCount: Long,
//        val totalCount: Long,
//    )
//
//    class NetworkAnalytics(
//        val id: String,
//        val title: String,
//        val analytics: ClientAnalyticsItem,
//    )
//
//    class CompanyAnalytics(
//        val id: String,
//        val title: String,
//        val analytics: ClientAnalyticsItem,
//    )
//

//
//    class Service(
//        val id: String,
//        val title: String,
//        val cost: Long,
//    )
//
//    class Staff(
//        val id: String,
//        val name: String,
//    )
//
//    class Time(
//        val date: LocalDate,
//        val start: LocalTime,
//        val end: LocalTime,
//    )
//
//    class Company(
//        val id: String,
//        val title: String,
//    )
//
//}