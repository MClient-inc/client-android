package ru.mclient.mvi.client.profile

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate
import java.time.LocalTime

interface ClientProfileStore :
    ParametrizedStore<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label, ClientProfileStore.Params> {

    data class Params(
        val clientId: Long,
        val companyId: Long?,
    )

    sealed class Intent {
        object Refresh : Intent()
    }


    data class State(
        val client: Client?,
        val abonements: List<ClientAbonement>?,
        val records: List<Record>?,
        val networkAnalytics: NetworkAnalytics?,
        val companyAnalytics: CompanyAnalytics?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) {

        data class Client(
            val id: Long,
            val name: String,
            val phone: String,
        )

        data class ClientAbonement(
            val id: Long,
            val usages: Int,
            val abonement: Abonement,
        )

        data class Abonement(
            val title: String,
            val subabonement: Subabonement,
        )

        data class Subabonement(
            val title: String,
            val maxUsages: Int,
        )

        //    Analytics

        data class ClientAnalyticsItem(
            var notComeCount: Long,
            var comeCount: Long,
            var waitingCount: Long,
            val totalCount: Long,
        )

        data class NetworkAnalytics(
            val id: Long,
            val title: String,
            val analytics: ClientAnalyticsItem,
        )

        data class CompanyAnalytics(
            val id: Long,
            val title: String,
            val analytics: ClientAnalyticsItem,
        )

        data class Record(
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

        data class Service(
            val id: Long,
            val title: String,
            val cost: Long,
        )

        data class Staff(
            val id: Long,
            val name: String,
        )

        data class Time(
            val date: LocalDate,
            val start: LocalTime,
            val end: LocalTime,
        )

        data class Company(
            val id: Long,
            val title: String,
        )

        sealed class CurrentPage {
            object Abonements : CurrentPage()
            object Records : CurrentPage()
            data class Analytics(val type: AnalyticsType) : CurrentPage() {
                enum class AnalyticsType {
                    COMPANY, NETWORK
                }
            }

        }

    }

    sealed class Label

}