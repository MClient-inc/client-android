package ru.mclient.mvi.staff.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface StaffProfileStore :
    ParametrizedStore<StaffProfileStore.Intent, StaffProfileStore.State, StaffProfileStore.Label, StaffProfileStore.Params> {

    @JvmInline
    value class Params(
        val staffId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    @Parcelize
    data class State(
        val staff: Staff?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Staff(
            val id: Long,
            val name: String,
            val codename: String,
            val role: String,
        ) : Parcelable
    }

    sealed class Label

}