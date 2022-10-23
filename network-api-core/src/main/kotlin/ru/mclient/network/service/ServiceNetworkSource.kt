package ru.mclient.network.service

interface ServiceNetworkSource {

    suspend fun getServicesForCategoryAndCompany(input: GetServicesForCategoryAndCompanyInput): GetServicesForCategoryAndCompanyOutput

    suspend fun createService(input: CreateServiceInput): CreateServiceOutput

}