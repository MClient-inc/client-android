package ru.mclient.ui.staff.list

import androidx.compose.runtime.Composable
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
                role = it.role.toDesignedString()
            )
        },
        isLoading = isLoading,
        isRefreshing = isLoading && staff.isNotEmpty()
    )
}

@Composable
fun StaffListUI(component: StaffList, modifier: Modifier) {
    StaffListPage(
        state = component.state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = { component.onSelect(it.id) },
        onCreate = component::onCreateStaff,
        modifier = modifier
    )
}
