package ru.mclient.network.staff

import java.time.LocalDateTime

data class GetStaffForCompanyInput(
    val companyId: Long,
)

data class GetStaffForCompanyOutput(
    val staff: List<Staff>,
) {
    data class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )
}

data class GetStaffForCompanyAndScheduleInput(
    val companyId: Long,
    val date: LocalDateTime,
)

data class GetStaffForCompanyAndScheduleOutput(
    val staff: List<Staff>,
) {
    data class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )
}

data class GetStaffByIdInput(
    val staffId: Long,
)

data class GetStaffByIdOutput(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)

data class CreateStaffInput(
    val companyId: Long,
    val name: String,
    val codename: String,
    val role: String,
)

data class CreateStaffOutput(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)