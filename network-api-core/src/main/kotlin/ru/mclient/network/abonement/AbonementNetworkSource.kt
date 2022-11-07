package ru.mclient.network.abonement

interface AbonementNetworkSource {

    suspend fun getAbonementById(input: GetAbonementByIdInput): GetAbonementByIdOutput

    suspend fun getAbonementsForCompany(input: GetAbonementsForCompanyInput): GetAbonementsForCompanyOutput

    suspend fun createAbonement(input: CreateAbonementInput): CreateAbonementOutput

}