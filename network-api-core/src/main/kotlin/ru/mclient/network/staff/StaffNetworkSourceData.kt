package ru.mclient.network.staff

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

data class CreateStaffScheduleInput(
    val staffId: Long,
    val schedule: List<Schedule>,
) {
    data class Schedule(
        val start: LocalDate,
        val end: LocalDate,
    )
}

data class CreateStaffScheduleOutput(
    val staffId: Long,
    val schedule: List<LocalDate>,
)


data class GetStaffScheduleByIdInput(
    val staffId: Long,
)

data class GetStaffScheduleByIdOutput(
    val schedule: List<Schedule>,
) {
    class Schedule(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )
}