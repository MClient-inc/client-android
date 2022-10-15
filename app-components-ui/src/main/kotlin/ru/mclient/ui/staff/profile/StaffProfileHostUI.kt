package ru.mclient.ui.staff.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.profile.StaffProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffProfileHostUI(
    component: StaffProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component) {
        StaffProfileUI(
            component = component.profile,
            modifier = modifier,
        )
    }
}