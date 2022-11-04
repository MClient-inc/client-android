package ru.mclient.common.client.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ClientProfileHostComponent(
    componentContext: DIComponentContext,
    clientId: Long,
) : ClientProfileHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState(title = "Клиент"))


    override val profile: ClientProfile = ClientProfileComponent(
        componentContext = childDIContext(key = "client_profile"),
        clientId = clientId
    )


}