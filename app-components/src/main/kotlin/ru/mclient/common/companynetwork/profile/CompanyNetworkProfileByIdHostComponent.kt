package ru.mclient.common.companynetwork.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState

class CompanyNetworkProfileByIdHostComponent(
    componentContext: DIComponentContext,
    networkId: Long,
) : CompanyNetworkProfileHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Сеть компаний"))

    override val profile: CompanyNetworkProfile =
        CompanyNetworkProfileByIdComponent(
            componentContext = componentContext,
            networkId = networkId,
        )

}