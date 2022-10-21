package ru.mclient.ui.bar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.bar.TopBar
import ru.mclient.ui.utils.defaultPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUI(
    component: TopBar,
    modifier: Modifier,
) {
    val state = component.state
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = state.title,
                modifier = Modifier.defaultPlaceholder(state.isLoading)
            )
        },
        modifier = modifier,
    )
}