package ru.mclient.common.abonement.profile


data class AbonnementProfileState(
    val abonnement: Abonnement?,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) {

    data class Abonnement(
        val name: String,
        val subAbonnements: List<SubAbonnement>,
        val services: List<Service>,
    )

    data class SubAbonnement(
        val name: String,
        val maxTimesNumberToUse: Int
    )

    data class Service(
        val title: String,
        val cost: Long
    )

}

interface AbonnementProfile {

    val state: AbonnementProfileState

    fun onRefresh()

}