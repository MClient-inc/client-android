package ru.mclient.network.client

interface ClientNetworkSource {


    suspend fun findClientsForCompany(input: GetClientsForCompanyInput): GetClientsForCompanyOutput

}