package ru.mclient.mvi.company.list

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface CompaniesListForNetworkStore :
    ParametrizedStore<CompaniesListForNetworkStore.Intent, CompaniesListForNetworkStore.State, CompaniesListForNetworkStore.Label, CompaniesListForNetworkStore.Params> {

    data class Params(
        val networkId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    @Parcelize
    data class State(
        val companies: List<Company>,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Company(
            val id: Long,
            val title: String,
            val codename: String,
            val icon: String?
        ) : Parcelable
    }

    sealed class Label

}