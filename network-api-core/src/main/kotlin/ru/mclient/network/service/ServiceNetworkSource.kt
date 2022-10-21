package ru.mclient.network.service

interface ServiceNetworkSource {

    suspend fun getServicesForCategoryAndCompany(input: GetServicesForCategoryAndCompanyInput): GetServicesForCategoryAndCompanyOutput

}