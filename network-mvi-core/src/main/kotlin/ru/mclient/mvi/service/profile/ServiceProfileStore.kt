package ru.mclient.mvi.service.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ServiceProfileStore :
    ParametrizedStore<ServiceProfileStore.Intent, ServiceProfileStore.State, ServiceProfileStore.Label, ServiceProfileStore.Params> {

    @JvmInline
    value class Params(
        val serviceId: Long
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    @Parcelize
    data class State(
        val service: Service?,
        val network: NetworkAnalytics?,
        val company: CompanyAnalytics?,
        val isFailure: Boolean,
        val isLoading: Boolean
    ) : Parcelable {
        @Parcelize
        data class Service(
            val id: Long,
            val title: String,
            val description: String,
            val cost: String
        ) : Parcelable
        @Parcelize
        data class AnalyticsItem(
            val comeCount: Long,
            val notComeCount: Long,
            val waitingCount: Long,
            val totalRecords: Long,
            val value: String
        ) : Parcelable
        @Parcelize
        data class NetworkAnalytics(
            val id: Long,
            val title: String,
            val analytics: AnalyticsItem,
        ) : Parcelable
        @Parcelize
        data class CompanyAnalytics(
            val id: Long,
            val title: String,
            val analytics: AnalyticsItem,
        ) : Parcelable
    }

    sealed class Label

}