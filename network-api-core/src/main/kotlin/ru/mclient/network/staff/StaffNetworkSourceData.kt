package ru.mclient.network.staff

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class GetStaffForCompanyInput(
    val companyId: String,
)

data class GetStaffForCompanyOutput(
    val staff: List<Staff>,
) {
    data class Staff(
        val id: String,
        val name: String,
        val codename: String,
        val role: String,
    )
}

data class GetStaffForCompanyAndScheduleInput(
    val companyId: String,
    val date: LocalDateTime,
)

data class GetStaffForCompanyAndScheduleOutput(
    val staff: List<Staff>,
) {
    data class Staff(
        val id: String,
        val name: String,
        val codename: String,
        val role: String,
    )
}

data class GetStaffByIdInput(
    val staffId: String,
)


data class GetStaffByIdOutput(
    val id: String,
    val name: String,
    val codename: String,
    val role: String,
)

data class CreateStaffInput(
    val companyId: String,
    val name: String,
    val codename: String,
    val role: String,
)

data class CreateStaffOutput(
    val id: String,
    val name: String,
    val codename: String,
    val role: String,
)

data class CreateStaffScheduleInput(
    val staffId: String,
    val schedule: List<Schedule>,
) {
    data class Schedule(
        val companyId: String,
        val start: LocalDate,
        val end: LocalDate,
    )
}

data class CreateStaffScheduleOutput(
    val staffId: String,
    val schedule: List<LocalDate>,
)


data class GetStaffScheduleByIdInput(
    val staffId: String,
    val companyId: String,
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