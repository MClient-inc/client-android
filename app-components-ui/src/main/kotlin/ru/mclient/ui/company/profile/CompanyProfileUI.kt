package ru.mclient.ui.company.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.company.profile.CompanyProfile
import ru.mclient.common.company.profile.CompanyProfileState
import ru.mclient.ui.view.toDesignedString

fun CompanyProfileState.toUI(): CompanyProfilePageState {
    return CompanyProfilePageState(
        profile = profile?.toUI(),
        isLoading = isLoading,
        isRefreshing = isLoading && profile != null
    )
}

fun CompanyProfileState.Profile.toUI(): CompanyProfilePageState.Profile {
    return CompanyProfilePageState.Profile(
        title = title.toDesignedString(),
        codename = codename.toDesignedString(),
        description = description.toDesignedString()
    )
}

@Composable
fun CompanyProfileUI(
    component: CompanyProfile,
    modifier: Modifier
) {
    CompanyProfilePage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onEdit = component::onEdit,
        onClients = component::onClients,
        onServices = component::onServices,
        onStaff = component::onStaff,
        onNetwork = component::onNetwork,
        modifier = modifier,
    )
}