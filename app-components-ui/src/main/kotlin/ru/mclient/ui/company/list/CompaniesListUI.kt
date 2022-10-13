package ru.mclient.ui.company.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ru.mclient.common.company.list.CompaniesList
import ru.mclient.common.company.list.CompaniesListState


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
        title = title,
        codename = codename,
        icon = icon
    )
}

fun CompaniesListPageState.Company.toDomain(): CompaniesListState.Company {
    return CompaniesListState.Company(
        id = id,
        title = title,
        codename = codename,
        icon = icon
    )
}

@Composable
fun CompaniesListUI(
    component: CompaniesList,
    modifier: Modifier,
) {
    CompaniesListPage(
        state = component.state.collectAsState().value.toUI(),
        onRefresh = component::onRefresh,
        onSelect = { component.onSelect(it.toDomain()) },
        modifier = modifier
    )
}