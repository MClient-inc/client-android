package ru.mclient.ui.record.create

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCreateItem(
    text: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(text)
        },
        trailingContent = {
            Icon(Icons.Outlined.ArrowForward, contentDescription = null)
        },
        modifier = modifier.clickable(enabled = isAvailable, onClick = onClick),
    )
}