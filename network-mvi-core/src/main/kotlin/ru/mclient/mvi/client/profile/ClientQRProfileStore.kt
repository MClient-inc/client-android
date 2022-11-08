package ru.mclient.mvi.client.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ClientQRProfileStore :
    ParametrizedStore<ClientQRProfileStore.Intent, ClientQRProfileStore.State, ClientQRProfileStore.Label, ClientQRProfileStore.Params> {

    @JvmInline
    value class Params(
        val clientId: Long,
    )

    sealed class Intent {

        data class Move(val isVisible: Boolean) : Intent()

    }

    @Parcelize
    data class State(
        val code: String?,
        val isLoading: Boolean,
        val isVisible: Boolean,
        val isFailure: Boolean,
    ) : Parcelable

    sealed class Label

}