package ru.mclient.mvi.record.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store
import java.time.LocalDateTime

interface RecordCreateStaffSelectorStore :
    Store<RecordCreateStaffSelectorStore.Intent, RecordCreateStaffSelectorStore.State, RecordCreateStaffSelectorStore.Label> {

    sealed class Intent {

        class Select(val staffId: Long, val staffName: String) : Intent()

        class Move(val isExpanded: Boolean) : Intent()

        class ChangeSchedule(val schedule: LocalDateTime?) : Intent()

    }

    @Parcelize
    data class State(
        val isExpanded: Boolean,
        val isAvailable: Boolean,
        val schedule: LocalDateTime?,
        val selectedStaff: Staff?,
    ) : Parcelable {
        @Parcelize
        class Staff(
            val id: Long,
            val name: String,
        ) : Parcelable
    }

    sealed class Label

}