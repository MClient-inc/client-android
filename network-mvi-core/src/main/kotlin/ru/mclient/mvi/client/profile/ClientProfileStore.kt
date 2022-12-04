package ru.mclient.mvi.client.profile

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate
import java.time.LocalTime

interface ClientProfileStore :
    ParametrizedStore<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label, ClientProfileStore.Params> {


    @JvmInline
    value class Params(
        val clientId: Long,
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
            val totalCost: Long
        )

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
            val id:Long,
            val title: String,
        )

    }

    sealed class Label

}