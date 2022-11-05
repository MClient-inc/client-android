package ru.mclient.ui.bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.bar.MergedHost
import ru.mclient.ui.fab.FabUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MergedHostUI(
    component: MergedHost,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarUI(component = component.bar, modifier = Modifier.fillMaxWidth())
        },
        floatingActionButton = {
            FabUI(component = component.fab)
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