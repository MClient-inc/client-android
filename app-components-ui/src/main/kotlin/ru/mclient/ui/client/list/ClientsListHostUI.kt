package ru.mclient.ui.client.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.client.list.ClientsListHost
import ru.mclient.ui.bar.MergedHostUI

@Composable
fun ClientsListHostUI(
    component: ClientsListHost,
    modifier: Modifier,
) {
    MergedHostUI(
        component = component,
        modifier = modifier,
    ) {
        ClientsListUI(
            component = component.list,
            modifier = Modifier.fillMaxSize()
        )
    }
}