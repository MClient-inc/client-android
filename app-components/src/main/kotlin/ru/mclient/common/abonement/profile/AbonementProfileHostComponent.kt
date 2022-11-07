package ru.mclient.common.abonement.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class AbonementProfileHostComponent(
    componentContext: DIComponentContext,
    abonementId: Long,
) : AbonementProfileHost, DIComponentContext by componentContext {

    override val abonementProfile: AbonementProfile = AbonementProfileComponent(
        componentContext = childDIContext(key = "abonement_profile"),
        abonementId = abonementId,
    )

    override val bar: TopBar = ImmutableTopBar(TopBarState("Абонемент"))

}