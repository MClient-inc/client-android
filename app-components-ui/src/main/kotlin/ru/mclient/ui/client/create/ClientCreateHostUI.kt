package ru.mclient.ui.client.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.create.ClientCreateHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ClientCreateHostUI(
    component: ClientCreateHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component, modifier = modifier) {
        ClientCreateUI(
            component = component.clientCreate,
            modifier = Modifier.fillMaxSize(),
        )
    }
}