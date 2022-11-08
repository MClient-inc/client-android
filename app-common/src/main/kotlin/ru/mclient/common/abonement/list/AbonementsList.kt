package ru.mclient.common.abonement.list


class AbonementsListState(
    val abonements: List<Abonement>,
    val isSubabonementClickable: Boolean,
    val isAbonementClickable: Boolean,
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
        val id: Long,
        val title: String,
        val cost: Long,
    )

}

interface AbonementsList {

    val state: AbonementsListState

    fun onRefresh()

    fun onSelect(abonementId: Long)

    fun onSelect(abonementId: Long, subabonementId: Long)

}