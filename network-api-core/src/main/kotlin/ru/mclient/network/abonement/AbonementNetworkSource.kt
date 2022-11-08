package ru.mclient.network.abonement

interface AbonementNetworkSource {

    suspend fun getAbonementById(input: GetAbonementByIdInput): GetAbonementByIdOutput

    suspend fun getAbonementsForCompany(input: GetAbonementsForCompanyInput): GetAbonementsForCompanyOutput

    suspend fun createAbonement(input: CreateAbonementInput): CreateAbonementOutput

    suspend fun getAbonementsForClient(input: GetAbonementsForClientInput): GetAbonementsForClientOutput

    suspend fun addAbonementForClient(input: AddAbonementToClientInput): AddAbonementToClientOutput

}