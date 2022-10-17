package ru.mclient.ui.staff.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.profile.StaffProfile
import ru.mclient.common.staff.profile.StaffProfileState

@Composable
fun StaffProfileUI(
    component: StaffProfile,
    modifier: Modifier,
) {
    val state by component.state.collectAsState()
    StaffProfilePage(
        state = state.toUI(),
        onEdit = component::onEdit,
        onRefresh = component::onRefresh,
        modifier = modifier,
    )
}


fun StaffProfileState.toUI(): StaffProfilePageState {
    return StaffProfilePageState(
        staff = staff?.toUI(),
        isRefreshing = staff != null && isLoading,
        isLoading = isLoading,
    )
}


fun StaffProfileState.Staff.toUI(): StaffProfilePageState.Staff {
    return StaffProfilePageState.Staff(name, codename, role)
}