package ru.mclient.common.abonement.clientcreate

import ru.mclient.common.bar.TopBarHost


class AbonementClientCreateHostState(
    val totalCost: Long,
    val isSuccess: Boolean,
    val isContinueAvailable: Boolean,
)

interface AbonementClientCreateHost : TopBarHost {

    val state: AbonementClientCreateHostState

    val profile: AbonementClientCreateClient

    val abonement: AbonementClientCreateAbonementSelector

    fun onContinue()

}