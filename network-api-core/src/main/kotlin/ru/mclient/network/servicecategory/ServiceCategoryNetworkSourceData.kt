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

data class GetServiceCategoryByIdInput(
    val categoryId: Long,
)

data class GetServiceCategoryByIdOutput(
    val id: Long,
    val title: String,
)

data class CreateServiceCategoryInput(
    val title: String,
    val companyId: Long,
)

data class CreateServiceCategoryOutput(
    val id: Long,
    val title: String,
)