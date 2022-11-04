package ru.mclient.ui.client.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.profile.ClientProfile
import ru.mclient.common.client.profile.ClientProfileState

@Composable
fun ClientProfileUI(
    component: ClientProfile,
    modifier: Modifier
) {
    ClientProfilePage(
        state = component.state.toUI(),
        onEdit = component::onEdit,
        onRefresh = component::onRefresh,
        modifier = modifier
    )
}

fun ClientProfileState.toUI(): ClientProfilePageState {
    return ClientProfilePageState(
        profile = client?.toUI(),
        isRefreshing = client != null && isLoading,
        isLoading = isLoading
    )
}

fun ClientProfileState.Client.toUI(): ClientProfilePageState.Profile {
    return ClientProfilePageState.Profile(name, phone)
}