package ru.mclient.mvi.abonement.clientcreate

import ru.mclient.mvi.ParametrizedStore

interface AbonementClientCreateClientStore :
    ParametrizedStore<AbonementClientCreateClientStore.Intent, AbonementClientCreateClientStore.State, AbonementClientCreateClientStore.Label, AbonementClientCreateClientStore.Params> {


    @JvmInline
    value class Params(
        val clientId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }


    data class State(
        val client: Client?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) {

        data class Client(
            val id: Long,
            val name: String,
            val phone: String,
        )

    }

    sealed class Label

}