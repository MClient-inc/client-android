package ru.mclient.ui.client.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.client.list.ClientsList
import ru.mclient.common.client.list.ClientsListState

fun ClientsListState.toUI(): ClientListPageState {
    return ClientListPageState(
        clients = clients.map {
            ClientListPageState.Client(id = it.id, title = it.title, phone = it.phone)
        },
        isLoading = isLoading,
        isRefreshing = isRefreshing
    )
}

@Composable
fun ClientsListUI(
    component: ClientsList,
    modifier: Modifier,
) {
    ClientsListPage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = remember(component) { { component.onClient(it.id) } },
        onCreate = component::onCreate,
        modifier = modifier,
    )
}