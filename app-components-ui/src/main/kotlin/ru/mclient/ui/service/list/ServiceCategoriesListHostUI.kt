package ru.mclient.ui.service.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.service.list.ServiceListHost
import ru.mclient.ui.bar.MergedHostUI

@Composable
fun ServiceListHostUI(
    component: ServiceListHost,
    modifier: Modifier,
) {
    MergedHostUI(component = component, modifier = modifier) {
        ServiceListUI(component = component.list, modifier = Modifier.fillMaxSize())
    }
}