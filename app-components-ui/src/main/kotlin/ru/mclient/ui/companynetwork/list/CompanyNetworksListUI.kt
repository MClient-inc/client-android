package ru.mclient.ui.companynetwork.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.companynetwork.list.CompanyNetworksList
import ru.mclient.common.companynetwork.list.CompanyNetworksListState


fun CompanyNetworksListState.toUI(): CompanyNetworksListPageState {
    return CompanyNetworksListPageState(
        companies = networks.map(CompanyNetworksListState.CompanyNetwork::toUI),
        isLoading = isLoading,
        isRefreshing = isLoading && networks.isNotEmpty()
    )
}

fun CompanyNetworksListState.CompanyNetwork.toUI(): CompanyNetworksListPageState.CompanyNetwork {
    return CompanyNetworksListPageState.CompanyNetwork(
        id = id,
        title = title,
        codename = codename,
        icon = icon
    )
}

fun CompanyNetworksListPageState.CompanyNetwork.toDomain(): CompanyNetworksListState.CompanyNetwork {
    return CompanyNetworksListState.CompanyNetwork(
        id = id,
        title = title,
        codename = codename,
        icon = icon
    )
}

@Composable
fun CompanyNetworksListUI(
    component: CompanyNetworksList,
    modifier: Modifier,
) {
    CompanyNetworksListPage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = { component.onSelect(it.toDomain()) },
        modifier = modifier
    )
}