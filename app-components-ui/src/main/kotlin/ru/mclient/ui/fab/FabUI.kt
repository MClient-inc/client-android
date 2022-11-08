package ru.mclient.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.fab.Fab
import ru.mclient.ui.view.ExtendOnShowFloatingActionButton

@Composable
fun FabUI(component: Fab, modifier: Modifier = Modifier) {
    ExtendOnShowFloatingActionButton(
        text = component.state.title,
        icon = if (component.state.isIconShown) Icons.Outlined.Add else null,
        isShown = component.state.isShown,
        onClick = component::onClick,
        isScrollInProgress = component.state.isScrollInProgress,
        modifier = modifier,
    )
}