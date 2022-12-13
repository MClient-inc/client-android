package ru.mclient.network.service

class GetServicesForCategoryAndCompanyInput(
    val companyId: String,
    val categoryId: String,
)

class GetServicesForCategoryAndCompanyOutput(
    val services: List<Service>,
) {

    class Service(
        val id: String,
        val categoryId: String,
        val title: String,
        val cost: Long,
        val formattedCost: String,
    )

}

class CreateServiceInput(
    val title: String,
    val cost: Long,
    val description: String,
    val categoryId: String,
    val companyId: String,
)

class CreateServiceOutput(
    val id: String,
    val title: String,
    val cost: String,
    val description: String,
    val categoryId: String,
)

data class GetServiceByIdInput(
    val serviceId: String,
)

data class GetServiceByIdOutput(
    val id: String,
    val title: String,
    val description: String,
    val cost: String,
)

data class GetServiceAnalyticsInput(
    val id: String,
    val companyId: String,
)

data class GetServiceAnalyticsOutput(
    val network: NetworkAnalytics,
    val company: CompanyAnalytics,
) {
    data class AnalyticsItem(
        val comeCount: Long,
        val notComeCount: Long,
        val waitingCount: Long,
        val totalRecords: Long,
        val popularity: String,
    )

    data class NetworkAnalytics(
        val id: String,
        val title: String,
        val analytics: AnalyticsItem,
    )

    data class CompanyAnalytics(
        val id: String,
        val title: String,
        val analytics: AnalyticsItem,
    )
}