package ru.mclient.ui.service.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.service.list.ServiceList
import ru.mclient.common.service.list.ServiceListState

fun ServiceListState.toUI(): ServicesListPageState {
    return ServicesListPageState(
        services = services.map(ServiceListState.Service::toUI),
        isLoading = isLoading,
        isRefreshing = isRefreshing,
    )
}

fun ServiceListState.Service.toUI(): ServicesListPageState.Service {
    return ServicesListPageState.Service(
        id = id,
        title = title,
    )
}

@Composable
fun ServiceListUI(component: ServiceList, modifier: Modifier) {
    ServicesListPage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = remember(component) { { component.onService(it.id) } },
        modifier = modifier,
    )
}