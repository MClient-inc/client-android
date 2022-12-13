package ru.mclient.common.service.profile

data class ServiceProfileState(
    val service: Service?,
    val network: NetworkAnalytics?,
    val company: CompanyAnalytics?,
    val analyticsType: AnalyticsType,
    val isTypeSelecting: Boolean,
    val isLoading: Boolean,
) {
    data class Service(
        val title: String,
        val cost: String,
        val description: String,
    )

    data class AnalyticsItem(
        val comeCount: Long,
        val notComeCount: Long,
        val waitingCount: Long,
        val totalRecords: Long,
        val popularity: String,
    )

    data class NetworkAnalytics(
        val id: Long,
        val title: String,
        val analytics: AnalyticsItem,
    )

    data class CompanyAnalytics(
        val id: Long,
        val title: String,
        val analytics: AnalyticsItem,
    )

    enum class AnalyticsType {
        COMPANY, NETWORK
    }

}

interface ServiceProfile {

    val state: ServiceProfileState

    fun onRefresh()

    fun onEdit()

    fun onDismiss()

    fun onToggleCompany()

    fun onToggleNetwork()

    fun onSelect()
}