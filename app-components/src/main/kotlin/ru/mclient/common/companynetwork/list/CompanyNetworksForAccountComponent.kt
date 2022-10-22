package ru.mclient.common.companynetwork.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.companynetwork.list.CompanyNetworksListForAccountStore

class CompanyNetworksForAccountComponent(
    componentContext: DIComponentContext,
    accountId: Long,
    private val onSelect: (CompanyNetworksListState.CompanyNetwork) -> Unit,
) : CompanyNetworksList, DIComponentContext by componentContext {

    private val store: CompanyNetworksListForAccountStore =
        getParameterizedStore { CompanyNetworksListForAccountStore.Params(accountId) }

    override val state: CompanyNetworksListState by store.states(this) { it.toState() }

    private fun CompanyNetworksListForAccountStore.State.toState(): CompanyNetworksListState {
        return CompanyNetworksListState(
            networks.map { company ->
                CompanyNetworksListState.CompanyNetwork(
                    id = company.id,
                    title = company.title,
                    codename = company.codename,
                    icon = company.icon
                )
            },
            isLoading = isLoading,
        )
    }

    override fun onRefresh() {
        store.accept(CompanyNetworksListForAccountStore.Intent.Refresh)
    }

    override fun onSelect(network: CompanyNetworksListState.CompanyNetwork) {
        onSelect.invoke(network)
    }
}