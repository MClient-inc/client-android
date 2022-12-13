package ru.mclient.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DesignedTabRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(10.dp, Alignment.Start),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .animateContentSize()
            .horizontalScroll(rememberScrollState()),
        content = content
    )
}

@Composable
fun DesignedTab(
    selected: Boolean,
    title: @Composable () -> Unit,
    onSelect: () -> Unit,
    label: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    ElevatedButton(
        onClick = onSelect,
        colors = buttonColors(selected),
        elevation = buttonElevation(selected),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            label()
            title()
            trailingContent()
        }
    }
}

@Composable
fun buttonElevation(selected: Boolean): ButtonElevation {
    val elevation = animateDpAsState(targetValue = if (selected) 1.dp else 0.dp)
    return ButtonDefaults.buttonElevation(
        defaultElevation = elevation.value,
        pressedElevation = 4.dp,
        focusedElevation = 3.dp,
        hoveredElevation = 3.dp,
        disabledElevation = 0.dp,
    )
}

@Composable
fun buttonColors(active: Boolean): ButtonColors {
    val containerColor = animateColorAsState(
        targetValue = if (active) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surface
    )
    val contentColor = animateColorAsState(
        targetValue = if (active) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )
    return ButtonDefaults.elevatedButtonColors(
        containerColor = containerColor.value,
        contentColor = contentColor.value
    )
}
