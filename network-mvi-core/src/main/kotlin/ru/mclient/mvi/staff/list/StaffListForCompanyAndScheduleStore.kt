package ru.mclient.mvi.staff.list

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDateTime

interface StaffListForCompanyAndScheduleStore :
    ParametrizedStore<StaffListForCompanyAndScheduleStore.Intent, StaffListForCompanyAndScheduleStore.State, StaffListForCompanyAndScheduleStore.Label, StaffListForCompanyAndScheduleStore.Params> {

    class Params(
        val companyId: Long,
        val schedule: LocalDateTime?,
    )

    sealed class Intent {

        object Refresh : Intent()

        class RefreshSchedule(val schedule: LocalDateTime?) : Intent()

    }

    data class State(
        val staff: List<Staff>,
        val schedule: LocalDateTime?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) {

        data class Staff(
            val id: Long,
            val name: String,
            val codename: String,
            val icon: String?,
            val role: String,
        )
    }

    sealed class Label

}