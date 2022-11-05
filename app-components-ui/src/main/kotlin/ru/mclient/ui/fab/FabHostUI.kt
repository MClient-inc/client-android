package ru.mclient.ui.fab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.fab.FabHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FabHostUI(
    component: FabHost,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FabUI(component = component.fab, modifier = Modifier)
        },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            content()
        }
    }
}