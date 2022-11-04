package ru.mclient.mvi.record.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store
import java.time.LocalTime

interface RecordCreateTimeSelectorStore :
    Store<RecordCreateTimeSelectorStore.Intent, RecordCreateTimeSelectorStore.State, RecordCreateTimeSelectorStore.Label> {

    sealed class Intent {

        class ChangeTme(val time: LocalTime?) : Intent()

    }

    @Parcelize
    data class State(
        val isAvailable: Boolean,
        val time: LocalTime?,
    ) : Parcelable

    sealed class Label

}