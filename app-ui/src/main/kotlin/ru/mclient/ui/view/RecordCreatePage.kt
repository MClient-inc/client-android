package ru.mclient.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignedCreateItem(
    text: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    supportingText: String? = null,
) {
    ListItem(
        leadingContent = icon?.let {
            {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                )
            }
        },
        headlineText = {
            Text(text)
        },
        supportingText = supportingText?.let {
            {
                Text(it)
            }
        },
        trailingContent = {
            IconButton(onClick = onClick, enabled = isAvailable) {
                Icon(Icons.Outlined.ArrowForward, contentDescription = null)
            }
        },
        modifier = modifier.clickable(enabled = isAvailable, onClick = onClick),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignedCreateItem(
    text: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    supportingText: String? = null,
) {
    ListItem(
        leadingContent = icon?.let {
            {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                )
            }
        },
        headlineText = {
            Text(text)
        },
        supportingText = supportingText?.let {
            {
                Text(it)
            }
        },
        trailingContent = {
            Icon(Icons.Outlined.ArrowForward, contentDescription = null)
        },
        modifier = modifier,
    )
}