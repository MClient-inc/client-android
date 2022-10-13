package ru.mclient.network.staff

interface StaffNetworkSource {


    suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput


}