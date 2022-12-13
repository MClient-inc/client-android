package ru.mclient.mvi.staff.profile

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDate
import java.time.LocalTime

interface StaffProfileStore :
    ParametrizedStore<StaffProfileStore.Intent, StaffProfileStore.State, StaffProfileStore.Label, StaffProfileStore.Params> {

    class Params(
        val staffId: Long,
        val companyId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    data class State(
        val staff: Staff?,
        val schedule: List<Schedule>,
        val isFailure: Boolean,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) {

        data class Staff(
            val id: Long,
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

    sealed class Label

}