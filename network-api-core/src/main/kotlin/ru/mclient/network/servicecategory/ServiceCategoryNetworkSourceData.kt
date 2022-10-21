package ru.mclient.network.servicecategory

class GetServiceCategoriesByCompanyInput(
    val companyId: Long,
)

class GetServiceCategoriesByCompanyOutput(
    val categories: List<ServiceCategory>,
) {
    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}
