package ru.mclient.network.company


interface CompanyNetworkSource {

    suspend fun createCompany(input: CompanyCreateInput): CompanyCreateOutput

    suspend fun getCompanies(input: GetCompaniesByNetworkInput): GetCompaniesByNetworkOutput

    suspend fun getNetworks(input: GetCompanyNetworksInput): GetCompanyNetworksOutput

}