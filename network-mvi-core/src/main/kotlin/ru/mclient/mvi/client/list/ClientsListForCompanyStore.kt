package ru.mclient.mvi.client.list

import ru.mclient.mvi.ParametrizedStore

interface ClientsListForCompanyStore :
    ParametrizedStore<ClientsListForCompanyStore.Intent, ClientsListForCompanyStore.State, ClientsListForCompanyStore.Label, ClientsListForCompanyStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val clients: List<Client>,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) {
        data class Client(
            val id: Long,
            val title: String,
            val phone: String,
        )
    }

    sealed class Label

}