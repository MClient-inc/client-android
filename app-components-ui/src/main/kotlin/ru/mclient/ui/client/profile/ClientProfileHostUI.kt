package ru.mclient.ui.client.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.client.profile.ClientProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ClientProfileHostUI(
    component: ClientProfileHost,
    modifier: Modifier
) {
    TopBarHostUI(component = component, modifier = modifier) {
        ClientProfileUI(
            component = component.profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        )
    }
}