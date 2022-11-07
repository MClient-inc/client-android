package ru.mclient.mvi.abonement.create

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.Store

interface AbonementCreateProfileStore :
    Store<AbonementCreateProfileStore.Intent, AbonementCreateProfileStore.State, AbonementCreateProfileStore.Label> {

    sealed class Intent {

        class Update(
            val title: String,
        ) : Intent()

    }

    @Parcelize
    data class State(
        val isSuccess: Boolean,
        val title: String,
    ) : Parcelable

    sealed class Label

}