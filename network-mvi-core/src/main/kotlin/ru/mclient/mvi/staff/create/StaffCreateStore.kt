package ru.mclient.mvi.staff.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface StaffCreateStore :
    ParametrizedStore<StaffCreateStore.Intent, StaffCreateStore.State, StaffCreateStore.Label, StaffCreateStore.Param> {

    @JvmInline
    value class Param(
        val companyId: Long
    )

    sealed class Intent {

        class Update(
            val name: String,
            val codename: String,
            val role: String,
        ) : Intent()

        class Create(
            val name: String,
            val codename: String,
            val role: String,
        ) : Intent()

    }

    @Parcelize
    data class State(
        val name: String,
        val codename: String,
        val role: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val createdStaff: Staff?,
        val isButtonEnabled: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Staff(
            val id: Long,
            val name: String,
            val codename: String,
            val role: String,
        ): Parcelable
    }

    sealed class Label

}