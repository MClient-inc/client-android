package ru.mclient.mvi.record.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface RecordCreateServicesSelectorStore :
    Store<RecordCreateServicesSelectorStore.Intent, RecordCreateServicesSelectorStore.State, RecordCreateServicesSelectorStore.Label> {

    sealed class Intent {

        class Select(val id: Long, val title: String, val cost: Long, val formattedCost: String) :
            Intent()

        class Move(val isExpanded: Boolean) : Intent()

        class DeleteById(val id: Int) : Intent()

    }

    @Parcelize
    data class State(
        val isExpanded: Boolean,
        val isAvailable: Boolean,
        val services: List<Service>,
    ) : Parcelable {
        @Parcelize
        data class Service(
            val id: Long,
            val title: String,
            val cost: Long,
            val formattedCost: String,
            val uniqueId: Int,
        ) : Parcelable
    }

    sealed class Label

}