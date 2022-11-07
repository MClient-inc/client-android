package ru.mclient.common.client.create

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext


class ClientCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSuccess: () -> Unit,
) : ClientCreateHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Создание клиента"))

    override val clientCreate: ClientCreate = ClientCreateComponent(
        componentContext = childDIContext("client_create"),
        companyId = companyId,
        onSuccess = { onSuccess() }
    )

}