package ru.mclient.mvi.home

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.mclient.mvi.ParametrizedStore

interface HomeStore :
    ParametrizedStore<HomeStore.Intent, HomeStore.State, HomeStore.Label, HomeStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    @Parcelize
    data class State(
        val company: Company?,
        val isLoading: Boolean,
        val isFirstLoading: Boolean,
        val isFailure: Boolean,
    ) : Parcelable {
        @Parcelize
        data class Company(
            val id: Long,
            val title: String,
        ) : Parcelable
    }

    sealed class Label

}