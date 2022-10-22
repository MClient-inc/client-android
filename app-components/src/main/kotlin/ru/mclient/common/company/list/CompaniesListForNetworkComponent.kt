package ru.mclient.common.company.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.company.list.CompaniesListForNetworkStore

class CompaniesListForNetworkComponent(
    componentContext: DIComponentContext,
    networkId: Long,
    private val onSelect: (CompaniesListState.Company) -> Unit,
) : CompaniesList, DIComponentContext by componentContext {

    private val store: CompaniesListForNetworkStore =
        getParameterizedStore { CompaniesListForNetworkStore.Params(networkId) }

    override val state: CompaniesListState by store.states(this) { it.toState() }

    private fun CompaniesListForNetworkStore.State.toState(): CompaniesListState {
        return CompaniesListState(
            companies.map { company ->
                CompaniesListState.Company(
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
        store.accept(CompaniesListForNetworkStore.Intent.Refresh)
    }

    override fun onSelect(companyId: Long) {
        state.companies.find { it.id == companyId }?.let { onSelect.invoke(it) }
    }

}