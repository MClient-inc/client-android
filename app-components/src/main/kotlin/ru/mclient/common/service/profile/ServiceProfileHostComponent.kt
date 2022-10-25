package ru.mclient.common.service.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ServiceProfileHostComponent(
    componentContext: DIComponentContext,
    serviceId: Long
) : ServiceProfileHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Услуга"))

    override val profile: ServiceProfile =
        ServiceProfileComponent(
            componentContext = childDIContext(key = "service_profile"), serviceId = serviceId
        )
}