package ru.mclient.common.companynetwork.list

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.mvi.companynetwork.list.CompanyNetworksListForAccountStore

class CompanyNetworksForAccountComponent(
    componentContext: DIComponentContext,
    accountId: Long,
    private val onSelect: (CompanyNetworksListState.CompanyNetwork) -> Unit,
) : CompanyNetworksList, DIComponentContext by componentContext {

    private val store: CompanyNetworksListForAccountStore =
        getParameterizedStore { CompanyNetworksListForAccountStore.Params(accountId) }

    override val state: StateFlow<CompanyNetworksListState> = store.states.map { it.toState() }
        .stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())

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