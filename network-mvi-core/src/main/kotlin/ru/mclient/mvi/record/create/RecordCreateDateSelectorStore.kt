package ru.mclient.mvi.record.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store
import java.time.LocalDate

interface RecordCreateDateSelectorStore :
    Store<RecordCreateDateSelectorStore.Intent, RecordCreateDateSelectorStore.State, RecordCreateDateSelectorStore.Label> {

    sealed class Intent {

        class ChangeDate(val date: LocalDate?) : Intent()

    }

    @Parcelize
    data class State(
        val isAvailable: Boolean,
        val date: LocalDate?,
    ) : Parcelable

    sealed class Label

}