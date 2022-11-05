package ru.mclient.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.fab.Fab
import ru.mclient.ui.view.ExtendOnShowFloatingActionButton
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString

@Composable
fun FabUI(component: Fab, modifier: Modifier = Modifier) {
    ExtendOnShowFloatingActionButton(
        text = "Добавить".toDesignedString(),
        icon = Icons.Outlined.Add.toDesignedDrawable(),
        isShown = component.state.isShown,
        onClick = component::onClick,
        isScrollInProgress = component.state.isScrollInProgress,
        modifier = modifier,
    )

}