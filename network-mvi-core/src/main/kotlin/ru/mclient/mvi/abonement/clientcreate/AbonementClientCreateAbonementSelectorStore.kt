package ru.mclient.mvi.abonement.clientcreate

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface AbonementClientCreateAbonementSelectorStore :
    Store<AbonementClientCreateAbonementSelectorStore.Intent, AbonementClientCreateAbonementSelectorStore.State, AbonementClientCreateAbonementSelectorStore.Label> {

    sealed class Intent {

        class Select(val abonement: Abonement) : Intent() {

            class Abonement(
                val id: Long,
                val title: String,
                val subabonement: Subabonement,
            )

            class Subabonement(
                val id: Long,
                val title: String,
                val cost: Long,
            )
        }

        class Move(val isExpanded: Boolean) : Intent()


    }

    @Parcelize
    data class State(
        val isExpanded: Boolean,
        val isAvailable: Boolean,
        val abonement: Abonement?,
    ) : Parcelable {

        @Parcelize
        class Abonement(
            val id: Long,
            val title: String,
            val subabonement: Subabonement,
        ) : Parcelable

        @Parcelize
        class Subabonement(
            val id: Long,
            val title: String,
            val cost: Long,
        ) : Parcelable
    }

    sealed class Label

}