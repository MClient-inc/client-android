package ru.mclient.network.staff

interface StaffNetworkSource {

    suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput

    suspend fun getStaffById(input: GetStaffByIdInput): GetStaffByIdOutput

    suspend fun createStaff(input: CreateStaffInput): CreateStaffOutput

}