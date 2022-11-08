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
    modifier: Modifier,
) {
    ClientQRProfileUI(component = component.clientQRProfile, modifier = modifier) {
        TopBarHostUI(component = component, modifier = Modifier.fillMaxSize()) {
            ClientProfileUI(
                component = component.profile,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            )
        }
    }
}
