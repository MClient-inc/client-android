package ru.mclient.network.client

interface ClientNetworkSource {


    suspend fun findClientsForCompany(input: GetClientsForCompanyInput): GetClientsForCompanyOutput

    suspend fun getClientById(input: GetClientByIdInput): GetClientByIdOutput

    suspend fun createClient(input: CreateClientInput): CreateClientOutput

    suspend fun getClientCard(input: GetClientCardInput): GetClientCardOutput

}