package ru.mclient.ui.bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.bar.TopBarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHostUI(
    component: TopBarHost,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarUI(component = component.bar, modifier = Modifier.fillMaxWidth())
        },
        floatingActionButton = floatingActionButton,
        modifier = modifier,
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            content()
        }
    }
}