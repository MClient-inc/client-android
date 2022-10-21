package ru.mclient.network.servicecategory

interface ServiceCategoryNetworkSource {

    suspend fun getServiceCategoriesByCompany(input: GetServiceCategoriesByCompanyInput): GetServiceCategoriesByCompanyOutput

}