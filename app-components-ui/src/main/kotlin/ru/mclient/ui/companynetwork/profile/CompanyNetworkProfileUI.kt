package ru.mclient.ui.companynetwork.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfile
import ru.mclient.common.companynetwork.profile.CompanyNetworkProfileState
import ru.mclient.ui.view.toDesignedString

fun CompanyNetworkProfileState.toUI(): CompanyNetworkProfilePageState {
    return CompanyNetworkProfilePageState(
        profile = network?.toUI(),
        isLoading = isLoading,
        isRefreshing = isLoading && network != null
    )
}

fun CompanyNetworkProfileState.CompanyNetwork.toUI(): CompanyNetworkProfilePageState.Profile {
    return CompanyNetworkProfilePageState.Profile(
        title = title.toDesignedString(),
        codename = codename.toDesignedString(),
        description = description.toDesignedString()
    )
}

@Composable
fun CompanyNetworkProfileUI(
    component: CompanyNetworkProfile,
    modifier: Modifier
) {
    val state by component.state
    CompanyNetworkProfilePage(
        state = state.toUI(),
        onRefresh = component::onRefresh,
        onEdit = component::onEdit,
        onClients = component::onClients,
        onServices = component::onServices,
        onStaff = component::onStaff,
        onCompany = component::onCompany,
        onAnalytics = component::onAnalytics,
        modifier = modifier,
    )
}