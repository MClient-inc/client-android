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

class CreateServiceInput(
    val title: String,
    val cost: String,
    val description: String,
    val categoryId: Long,
    val companyId: Long,
)

class CreateServiceOutput(
    val id: Long,
    val title: String,
    val cost: String,
    val description: String,
    val categoryId: Long,
)