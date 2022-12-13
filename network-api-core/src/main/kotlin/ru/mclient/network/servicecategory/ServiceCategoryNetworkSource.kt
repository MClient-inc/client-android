package ru.mclient.network.servicecategory

interface ServiceCategoryNetworkSource {

    suspend fun getServiceCategoriesByCompany(input: GetServiceCategoriesByCompanyInput): GetServiceCategoriesByCompanyOutput

    suspend fun getServiceCategoryById(input: GetServiceCategoryByIdInput): GetServiceCategoryByIdOutput

    suspend fun createServiceCategory(input: CreateServiceCategoryInput): CreateServiceCategoryOutput

}