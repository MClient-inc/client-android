package ru.mclient.mvi.record.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface RecordCreateClientSelectorStore :
    Store<RecordCreateClientSelectorStore.Intent, RecordCreateClientSelectorStore.State, RecordCreateClientSelectorStore.Label> {

    sealed class Intent {

        class Select(val clientId: Long, val clientName: String) : Intent()

        class Move(val isExpanded: Boolean) : Intent()

    }

    @Parcelize
    data class State(
        val isExpanded: Boolean,
        val isAvailable: Boolean,
        val selectedClient: Client?,
    ) : Parcelable {
        @Parcelize
        class Client(
            val id: Long,
            val name: String,
        ) : Parcelable
    }

    sealed class Label

}