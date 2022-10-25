package ru.mclient.ui.client.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.list.ClientsListHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ClientsListHostUI(
    component: ClientsListHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component) {
        ClientsListUI(
            component = component.list,
            modifier = modifier,
        )
    }
}