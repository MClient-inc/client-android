package ru.mclient.network.servicecategory

class GetServiceCategoriesByCompanyInput(
    val companyId: String,
)

class GetServiceCategoriesByCompanyOutput(
    val categories: List<ServiceCategory>,
) {
    data class ServiceCategory(
        val id: String,
        val title: String,
    )
}

data class GetServiceCategoryByIdInput(
    val categoryId: String,
)

data class GetServiceCategoryByIdOutput(
    val id: String,
    val title: String,
)

data class CreateServiceCategoryInput(
    val title: String,
    val companyId: String,
)

data class CreateServiceCategoryOutput(
    val id: String,
    val title: String,
)