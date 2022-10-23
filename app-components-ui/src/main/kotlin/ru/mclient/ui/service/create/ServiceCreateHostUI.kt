package ru.mclient.ui.service.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.service.create.ServiceCreateHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ServiceCreateHostUI(
    component: ServiceCreateHost,
    modifier: Modifier
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        ServiceCreateUI(
            component = component.serviceCreate,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
        )
    }
}