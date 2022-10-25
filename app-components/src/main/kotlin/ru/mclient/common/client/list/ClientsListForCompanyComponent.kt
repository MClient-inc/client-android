package ru.mclient.common.client.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.client.list.ClientsListForCompanyStore

class ClientsListForCompanyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onClient: (SelectedClient) -> Unit,
    private val onCreate: () -> Unit,
) : ClientsList, DIComponentContext by componentContext {

    private val store: ClientsListForCompanyStore =
        getParameterizedStore { ClientsListForCompanyStore.Params(companyId) }

    override val state: ClientsListState by store.states(this) { it.toState() }

    private fun ClientsListForCompanyStore.State.toState(): ClientsListState {
        return ClientsListState(
            clients = clients.map {
                ClientsListState.Client(
                    id = it.id,
                    title = it.title,
                    phone = it.phone
                )
            },
            isLoading = isLoading,
            isRefreshing = clients.isNotEmpty() && isLoading,
        )
    }

    override fun onRefresh() {
        store.accept(ClientsListForCompanyStore.Intent.Refresh)
    }

    override fun onClient(clientId: Long) {
        onClient.invoke(SelectedClient(clientId))
    }

    override fun onCreate() {
        onCreate.invoke()
    }

    @JvmInline
    value class SelectedClient(
        val id: Long
    )

}