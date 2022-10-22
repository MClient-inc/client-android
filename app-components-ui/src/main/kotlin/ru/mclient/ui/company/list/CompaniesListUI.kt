package ru.mclient.ui.company.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.company.list.CompaniesList
import ru.mclient.common.company.list.CompaniesListState
import ru.mclient.ui.view.toDesignedString


fun CompaniesListState.toUI(): CompaniesListPageState {
    return CompaniesListPageState(
        companies = companies.map(CompaniesListState.Company::toUI),
        isLoading = isLoading,
        isRefreshing = isLoading && companies.isNotEmpty()
    )
}

fun CompaniesListState.Company.toUI(): CompaniesListPageState.Company {
    return CompaniesListPageState.Company(
        id = id,
        title = title.toDesignedString(),
        codename = codename.toDesignedString(),
        icon = null
    )
}

@Composable
fun CompaniesListUI(
    component: CompaniesList,
    modifier: Modifier,
) {
    CompaniesListPage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = { component.onSelect(it.id) },
        modifier = modifier
    )
}