package ru.mclient.common.client.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ClientsListForCompanyHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onClient: (SelectedClient) -> Unit,
    onCreate: () -> Unit,
) : ClientsListHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Клиентская база"))
    override val list: ClientsList = ClientsListForCompanyComponent(
        componentContext = childDIContext(key = "clients_list"),
        companyId = companyId,
        onClient = { onClient.invoke(SelectedClient(it.id)) },
        onCreate = onCreate,
    )

    @JvmInline
    value class SelectedClient(
        val id: Long
    )

}