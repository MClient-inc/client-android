package ru.mclient.mvi.modal

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ModalStore :
    ParametrizedStore<ModalStore.Intent, ModalStore.State, ModalStore.Label, ModalStore.Params> {

    @JvmInline
    value class Params(val isVisible: Boolean)

    data class Intent(val isVisible: Boolean)

    @Parcelize
    data class State(
        val isVisible: Boolean,
    ) : Parcelable

    data class Label(val isVisible: Boolean)

}