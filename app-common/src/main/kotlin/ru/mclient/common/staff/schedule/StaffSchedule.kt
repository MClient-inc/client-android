package ru.mclient.common.staff.schedule

import java.time.LocalDate

class StaffScheduleState(
    val selectedSchedule: List<Schedule>,
    val isLoading: Boolean,
    val isAvailable: Boolean,
) {
    class Schedule(val start: LocalDate, val end: LocalDate)
}


interface StaffSchedule {

    val state: StaffScheduleState

    fun onSelect(start: LocalDate, end: LocalDate)

    fun onSelect(date: LocalDate)

    fun onContinue()
}