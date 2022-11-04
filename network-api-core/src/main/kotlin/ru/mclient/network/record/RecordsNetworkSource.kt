package ru.mclient.network.record

interface RecordsNetworkSource {

    suspend fun getRecordsForCompany(input: GetRecordsForCompanyInput): GetRecordsForCompanyOutput

    suspend fun createRecord(input: CreateRecordInput): CreateRecordOutput


}