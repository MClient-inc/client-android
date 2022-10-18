package ru.mclient.mvi.companynetwork.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface CompanyNetworkProfileStore :
    ParametrizedStore<CompanyNetworkProfileStore.Intent, CompanyNetworkProfileStore.State, CompanyNetworkProfileStore.Label, CompanyNetworkProfileStore.Params> {

    data class Params(
        val networkId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    @Parcelize
    data class State(
        val network: CompanyNetwork?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class CompanyNetwork(
            val id: Long,
            val title: String,
            val codename: String,
            val description: String,
            val icon: String?
        ) : Parcelable
    }

    sealed class Label

}