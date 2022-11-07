package ru.mclient.mvi.abonement.list

import ru.mclient.mvi.ParametrizedStore

interface AbonementsListStore :
    ParametrizedStore<AbonementsListStore.Intent, AbonementsListStore.State, AbonementsListStore.Label, AbonementsListStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val abonements: List<Abonement>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val isFailure: Boolean,
    ) {

        data class Abonement(
            val id: Long,
            val title: String,
            val subabonements: List<Subabonement>,
        )

        data class Subabonement(
            val title: String,
        )

    }

    sealed class Label

}