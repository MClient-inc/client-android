package ru.mclient.common.abonement.clientcreate

import ru.mclient.common.abonement.list.AbonementsList

data class AbonementClientCreateAbonementSelectorState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val isSuccess: Boolean,
    val abonement: Abonement?,
) {

    data class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )

    data class Subabonement(
        val id: Long,
        val title: String,
        val cost: Long,
    )

}

interface AbonementClientCreateAbonementSelector {

    val state: AbonementClientCreateAbonementSelectorState

    val selector: AbonementsList

    fun onDismiss()

    fun onExpand()

}