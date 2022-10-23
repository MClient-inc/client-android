package ru.mclient.ui.staff.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.list.StaffListHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffListHostUI(
    component: StaffListHost,
    modifier: Modifier
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        StaffListUI(
            component = component.list,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}