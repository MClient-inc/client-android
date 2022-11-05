package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.staff.profile.StaffProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun StaffProfileHostUI(
    component: StaffProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component, modifier = modifier) {
        StaffProfileUI(
            component = component.profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
        )
    }
}