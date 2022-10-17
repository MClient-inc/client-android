package ru.mclient.mvi.staff.list

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface StaffListForCompanyStore :
    ParametrizedStore<StaffListForCompanyStore.Intent, StaffListForCompanyStore.State, StaffListForCompanyStore.Label, StaffListForCompanyStore.Params> {

    @JvmInline
    value class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    @Parcelize
    data class State(
        val staff: List<Staff>,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Staff(
            val id: Long,
            val name: String,
            val codename: String,
            val icon: String?,
            val role: String,
        ) : Parcelable
    }

    sealed class Label

}