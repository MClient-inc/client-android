package ru.mclient.network.record

interface RecordsNetworkSource {


    suspend fun getRecordsForCompany(input: GetRecordsForCompanyInput): GetRecordsForCompanyOutput

}