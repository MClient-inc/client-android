package ru.mclient.mvi.companynetwork.list

import ru.mclient.mvi.ParametrizedStore

interface CompanyNetworksListForAccountStore :
    ParametrizedStore<CompanyNetworksListForAccountStore.Intent, CompanyNetworksListForAccountStore.State, CompanyNetworksListForAccountStore.Label, CompanyNetworksListForAccountStore.Params> {

    data class Params(
        val accountId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val networks: List<CompanyNetwork>,
        val accountId: Long,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) {

        data class CompanyNetwork(
            val id: Long,
            val title: String,
            val codename: String,
            val icon: String?
        )
    }

    sealed class Label

}