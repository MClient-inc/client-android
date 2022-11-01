package ru.mclient.ui.service.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.service.profile.ServiceProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ServiceProfileHostUI(
    component: ServiceProfileHost,
    modifier: Modifier
) {
    TopBarHostUI(component = component, modifier = modifier) {
        ServiceProfileUI(
            component = component.profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        )
    }
}