package ru.mclient.mvi.staff.schedule

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate

interface StaffScheduleEditStore :
    ParametrizedStore<StaffScheduleEditStore.Intent, StaffScheduleEditStore.State, StaffScheduleEditStore.Label, StaffScheduleEditStore.Params> {

    class Params(
        val staffId: Long,
    )

    sealed class Intent {

        class Select(val start: LocalDate, val end: LocalDate) : Intent()

        object Apply : Intent()

    }

    data class State(
        val selectedSchedule: List<Schedule>,
        val isAvailable: Boolean,
        val isLoading: Boolean,
        val isSuccess: Boolean,
    ) {
        class Schedule(val start: LocalDate, val end: LocalDate)
    }

    sealed class Label

}