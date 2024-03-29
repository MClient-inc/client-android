package ru.mclient.network.record

interface RecordsNetworkSource {

    suspend fun getRecordsForCompany(input: GetRecordsForCompanyInput): GetRecordsForCompanyOutput

    suspend fun createRecord(input: CreateRecordInput): CreateRecordOutput

    suspend fun getRecordById(input: GetRecordByIdInput): GetRecordByIdOutput

    suspend fun editRecordStatus(input: EditRecordStatusInput): EditRecordStatusOutput

    suspend fun payWithAbonements(input: PayWithAbonementsInput): PayWithAbonementsOutput

}