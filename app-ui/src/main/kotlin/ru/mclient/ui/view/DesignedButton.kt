package ru.mclient.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DesignedButton(
    text: DesignedString,
    onClick: () -> Unit,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        DesignedText(
            text = text,
            overflow = overflow,
            maxLines = maxLines,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun DesignedButton(
    text: String,
    onClick: () -> Unit,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(
            text = text,
            overflow = overflow,
            maxLines = maxLines,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun DesignedFilledButton(
    text: String,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(
            text = text,
            overflow = overflow,
            maxLines = maxLines,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}