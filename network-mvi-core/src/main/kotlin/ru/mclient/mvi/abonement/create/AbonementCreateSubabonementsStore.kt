package ru.mclient.mvi.abonement.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface AbonementCreateSubabonementsStore :
    Store<AbonementCreateSubabonementsStore.Intent, AbonementCreateSubabonementsStore.State, AbonementCreateSubabonementsStore.Label> {

    sealed class Intent {


        object Create : Intent()

        class Update(
            val title: String,
            val usages: String,
            val cost: String,
        ) : Intent()

        class DeleteById(val id: Int) : Intent()

    }

    @Parcelize
    data class State(
        val isSuccess: Boolean,
        val subabonements: List<Subabonement>,
        val creation: Creation,
    ) : Parcelable {

        @Parcelize
        data class Subabonement(
            val title: String,
            val usages: Int,
            val cost: Long,
            val uniqueId: Int,
        ) : Parcelable

        @Parcelize
        data class Creation(
            val title: String,
            val usages: Int,
            val cost: Long,
            val isAvailable: Boolean,
            val isContinueAvailable: Boolean,
        ) : Parcelable


    }

    sealed class Label

}