package ru.mclient.ui.service.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.service.profile.ServiceProfile
import ru.mclient.common.service.profile.ServiceProfileState

@Composable
fun ServiceProfileUI(
    component : ServiceProfile,
    modifier: Modifier
) {
    ServiceProfilePage(
        state = component.state.toUI(),
        onEdit = component::onEdit,
        onRefresh = component::onRefresh,
        modifier = modifier
    )
}

fun ServiceProfileState.toUI(): ServiceProfilePageState {
    return ServiceProfilePageState(
        service = service?.toUI(),
        isRefreshing = service != null && isLoading,
        isLoading = isLoading
    )
}

fun ServiceProfileState.Service.toUI(): ServiceProfilePageState.Service {
    return ServiceProfilePageState.Service(title, description, cost)
}