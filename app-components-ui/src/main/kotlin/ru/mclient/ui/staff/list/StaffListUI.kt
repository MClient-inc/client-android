package ru.mclient.ui.staff.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.list.StaffList
import ru.mclient.common.staff.list.StaffListState
import ru.mclient.ui.view.toDesignedString

fun StaffListState.toUI(): StaffListPageState {
    return StaffListPageState(
        staff = staff.map {
            StaffListPageState.Staff(
                id = it.id,
                name = it.name.toDesignedString(),
                codename = it.codename.toDesignedString(),
                icon = null,
            )
        },
        isLoading = isLoading,
        isRefreshing = isLoading && staff.isEmpty()
    )
}

@Composable
fun StaffListUI(component: StaffList, modifier: Modifier) {
    val state by component.state.collectAsState()
    StaffListPage(
        state = state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = { component.onSelect(it.id) },
        modifier = modifier
    )
}
