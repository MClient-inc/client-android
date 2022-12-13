package ru.mclient.mvi.service.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ServiceProfileStore :
    ParametrizedStore<ServiceProfileStore.Intent, ServiceProfileStore.State, ServiceProfileStore.Label, ServiceProfileStore.Params> {

    data class Params(
        val serviceId: Long,
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

        object ToggleCompany : Intent()

        object ToggleNetwork : Intent()

        object Dismiss : Intent()

        object Select : Intent()

    }

    @Parcelize
    data class State(
        val service: Service?,
        val network: NetworkAnalytics?,
        val company: CompanyAnalytics?,
        val analyticsType: AnalyticsType,
        val isTypeSelecting: Boolean,
        val isFailure: Boolean,
        val isLoading: Boolean,
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
            val popularity: String,
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

        enum class AnalyticsType {
            COMPANY, NETWORK
        }
    }

    sealed class Label

}