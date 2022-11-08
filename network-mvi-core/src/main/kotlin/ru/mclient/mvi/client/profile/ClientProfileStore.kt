package ru.mclient.mvi.client.profile

import ru.mclient.mvi.ParametrizedStore

interface ClientProfileStore :
    ParametrizedStore<ClientProfileStore.Intent, ClientProfileStore.State, ClientProfileStore.Label, ClientProfileStore.Params> {


    @JvmInline
    value class Params(
        val clientId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }


    data class State(
        val client: Client?,
        val abonements: List<ClientAbonement>?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) {

        data class Client(
            val id: Long,
            val name: String,
            val phone: String,
        )

        class ClientAbonement(
            val id: Long,
            val usages: Int,
            val abonement: Abonement,
        )

        class Abonement(
            val title: String,
            val subabonement: Subabonement,
        )

        class Subabonement(
            val title: String,
            val maxUsages: Int,
        )


    }

    sealed class Label

}