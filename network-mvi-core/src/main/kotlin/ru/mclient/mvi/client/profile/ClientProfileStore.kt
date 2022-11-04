package ru.mclient.mvi.client.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface ClientProfileStore:
    ParametrizedStore<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label, ClientProfileStore.Params> {


    @JvmInline
    value class Params(
        val clientId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    @Parcelize
    data class State(
        val client: Client?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Client(
            val id: Long,
            val name: String,
            val phone: String,
        ) : Parcelable
    }

    sealed class Label

}