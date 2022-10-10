package ru.mclient.mvi.companynetwork.list

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface CompanyNetworksListForAccountStore :
    ParametrizedStore<CompanyNetworksListForAccountStore.Intent, CompanyNetworksListForAccountStore.State, CompanyNetworksListForAccountStore.Label, CompanyNetworksListForAccountStore.Params> {

    data class Params(
        val accountId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    @Parcelize
    data class State(
        val networks: List<CompanyNetwork>,
        val accountId: Long,
        val isLoading: Boolean,
        val isFailure: Boolean,
    ) : Parcelable {
        @Parcelize
        data class CompanyNetwork(
            val id: Long,
            val title: String,
            val codename: String,
            val icon: String?
        ) : Parcelable
    }

    sealed class Label

}