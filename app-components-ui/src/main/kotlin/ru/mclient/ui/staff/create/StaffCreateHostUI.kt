package ru.mclient.ui.staff.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.create.StaffCreateHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffCreateHostUI(
    component: StaffCreateHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component) {
        StaffCreateUI(
            component = component.staffCreate,
            modifier = modifier,
        )
    }
}