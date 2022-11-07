package ru.mclient.mvi.abonnement.profile

import ru.mclient.mvi.ParametrizedStore

interface AbonnementProfileStore :
    ParametrizedStore<AbonnementProfileStore.Intent, AbonnementProfileStore.State, AbonnementProfileStore.Label, AbonnementProfileStore.Params> {

    @JvmInline
    value class Params(
        val abonnementId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    data class State(
        val abonnement: Abonnement?,
        val isFailure: Boolean,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) {

        data class Abonnement(
            val id: Long,
            val name: String,
            val subAbonnements: List<SubAbonnement>,
            val services: List<Service>,
        )

        data class SubAbonnement(
            val id: Long,
            val name: String,
            val maxTimesNumberToUse: Int
        )

        data class Service(
            val id: Long,
            val title: String,
            val cost: Long
        )


    }

    sealed class Label

}