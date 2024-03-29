package ru.mclient.ui.staff.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.staff.create.StaffCreateHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffCreateHostUI(
    component: StaffCreateHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        StaffCreateUI(
            component = component.staffCreate,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
        )
    }
}