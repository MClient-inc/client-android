package ru.mclient.network.staff

interface StaffNetworkSource {

    suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput

    suspend fun getStaffForCompanyAndSchedule(input: GetStaffForCompanyAndScheduleInput): GetStaffForCompanyAndScheduleOutput

    suspend fun getStaffById(input: GetStaffByIdInput): GetStaffByIdOutput

    suspend fun getStaffSchedule(input: GetStaffScheduleByIdInput): GetStaffScheduleByIdOutput

    suspend fun createStaff(input: CreateStaffInput): CreateStaffOutput

    suspend fun createStaffSchedule(input: CreateStaffScheduleInput): CreateStaffScheduleOutput

}