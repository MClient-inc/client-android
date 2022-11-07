package ru.mclient.common.abonement.profile


data class AbonementProfileState(
    val abonement: Abonement?,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {

    data class Abonement(
        val title: String,
        val subabonements: List<Subabonement>,
        val services: List<Service>,
    )

    data class Subabonement(
        val title: String,
        val maxTimesNumberToUse: Int,
    )

    data class Service(
        val title: String,
        val cost: Long,
    )

}

interface AbonementProfile {

    val state: AbonementProfileState

    fun onRefresh()

}