package ru.mclient.common.client.profile

import java.time.LocalDate
import java.time.LocalTime

class ClientProfileState(
    val client: Client?,
    val abonements: List<ClientAbonement>?,
    val networkAnalytics: NetworkAnalytics?,
    val companyAnalytics: CompanyAnalytics?,
    val records: List<Record>?,
    val isLoading: Boolean,
) {
    class Client(
        val phone: String,
        val name: String,
    )

    class ClientAbonement(
        val id: Long,
        val usages: Int,
        val abonement: Abonement,
    )

    class Abonement(
        val title: String,
        val subabonement: Subabonement,
    )

    class Subabonement(
        val title: String,
        val maxUsages: Int,
    )

//    Analytics

    class ClientAnalyticsItem(
        var notComeCount: Long,
        var comeCount: Long,
        var waitingCount: Long,
        val totalCount: Long,
    )

    class NetworkAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    class CompanyAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    class Record(
        val id: Long,
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

    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    class Staff(
        val id: Long,
        val name: String,
    )

    class Time(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )

    class Company(
        val id: Long,
        val title: String,
    )


}

interface ClientProfile {

    val state: ClientProfileState

    fun onRefresh()

    fun onEdit()

    fun onAbonementCreate()

    fun onQRCode()

    fun onRecord(recordId: Long)

}