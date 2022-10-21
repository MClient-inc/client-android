package ru.mclient.network.service

class GetServicesForCategoryAndCompanyInput(
    val companyId: Long,
    val categoryId: Long,
)

class GetServicesForCategoryAndCompanyOutput(
    val services: List<Service>,
) {

    class Service(
        val id: Long,
        val categoryId: Long,
        val title: String,
    )

}