package ru.mclient.ui.view

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ExtendOnShowFloatingActionButton(
    text: DesignedString,
    icon: DesignedDrawable,
    isShown: Boolean,
    onClick: () -> Unit,
    isScrollInProgress: Boolean = false,
    modifier: Modifier = Modifier,
) {
    if (isShown) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit) {
            isExpanded = true
        }
        ExtendedFloatingActionButton(
            text = {
                DesignedText(text)
            },
            icon = { DesignedIcon(icon = icon) },
            onClick = onClick,
            expanded = isExpanded && !isScrollInProgress,
            modifier = modifier
        )
    }
}

@Composable
fun ExtendOnShowFloatingActionButton(
    text: String,
    icon: ImageVector?,
    isShown: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isScrollInProgress: Boolean = false,
) {
    if (isShown) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit) {
            isExpanded = true
        }
        ExtendedFloatingActionButton(
            text = {
                Text(text)
            },
            icon = {
                if (icon != null) {
                    Icon(icon, contentDescription = null)
                }
            },
            onClick = onClick,
            expanded = isExpanded && !isScrollInProgress,
            modifier = modifier
        )
    }
}