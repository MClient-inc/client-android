package ru.mclient.common.abonement.create

import ru.mclient.common.bar.TopBarHost
import ru.mclient.common.service.list.ServicesListSelector

data class AbonementCreateHostState(
    val isButtonAvailable: Boolean,
)

interface AbonementCreateHost : TopBarHost {

    val state: AbonementCreateHostState

    val profile: AbonementCreateProfile

    val subabonements: AbonementCreateSubabonements

    val services: ServicesListSelector

    fun onContinue()

}