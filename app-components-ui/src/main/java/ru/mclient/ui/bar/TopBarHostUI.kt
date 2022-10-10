package ru.mclient.ui.bar

import androidx.compose.foundation.layout.Box
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
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarUI(component = component.bar, modifier = Modifier.fillMaxWidth())
        },
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()
        }
    }
}