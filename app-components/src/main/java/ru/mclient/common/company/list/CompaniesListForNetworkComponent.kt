package ru.mclient.common.company.list

import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.mvi.company.list.CompaniesListForNetworkStore

class CompaniesListForNetworkComponent(
    componentContext: DIComponentContext,
    networkId: Long,
    private val onSelect: (CompaniesListState.Company) -> Unit,
) : CompaniesList, DIComponentContext by componentContext {

    private val store: CompaniesListForNetworkStore =
        getParameterizedStore { CompaniesListForNetworkStore.Params(networkId) }

    override val state: StateFlow<CompaniesListState> = store.states.map { it.toState() }
        .stateIn(componentScope, SharingStarted.Eagerly, store.state.toState())

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

    override fun onSelect(company: CompaniesListState.Company) {
        onSelect.invoke(company)
    }

}