package ru.mclient.common.staff.profile

import java.time.LocalDate
import java.time.LocalTime

data class StaffProfileState(
    val staff: Staff?,
    val schedule: List<Schedule>?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Staff(
        val name: String,
        val codename: String,
        val role: String,
    )


    class Schedule(
        val date: LocalDate,
        val start: LocalTime,
        val end: LocalTime,
    )


}


interface StaffProfile {

    val state: StaffProfileState

    fun onRefresh()

    fun onEditProfile()

    fun onEditSchedule()

}