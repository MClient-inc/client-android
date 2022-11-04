package ru.mclient.common.client.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab

class ClientsListForCompanyHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onClient: (SelectedClient) -> Unit,
    private val onCreate: () -> Unit,
) : ClientsListHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Клиентская база"))

    override val list: ClientsList = ClientsListForCompanyComponent(
        componentContext = childDIContext(key = "clients_list"),
        companyId = companyId,
        onClient = { onClient.invoke(SelectedClient(it.id)) },
    )

    override val fab: Fab
        get() = ImmutableFab(
            FabState(
                title = "Добавить",
                isShown = !list.state.isLoading || list.state.clients.isNotEmpty(),
                isScrollInProgress = false
            ), onClick = onCreate
        )


    @JvmInline
    value class SelectedClient(
        val id: Long,
    )

}