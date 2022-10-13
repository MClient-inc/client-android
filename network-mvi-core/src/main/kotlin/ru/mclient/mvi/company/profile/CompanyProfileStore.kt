package ru.mclient.mvi.company.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface CompanyProfileStore :
    ParametrizedStore<CompanyProfileStore.Intent, CompanyProfileStore.State, CompanyProfileStore.Label, CompanyProfileStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    @Parcelize
    data class State(
        val company: Company?,
        val isFailure: Boolean,
        val isLoading: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Company(
            val id: Long,
            val title: String,
            val codename: String,
            val description: String,
            val icon: String?
        ) : Parcelable
    }

    sealed class Label

}