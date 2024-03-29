package ru.mclient.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DesignedOutlinedTitledBlock(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 15.dp)
        )
        Box(modifier = Modifier.outlined()) {
            content()
        }
    }
}

@Composable
fun DesignedOutlinedTitledBlock(
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            )
            trailingContent()
        }
        Box(modifier = Modifier.outlined()) {
            content()
        }
    }
}


@Composable
fun DesignedOutlinedTitledBlock(
    title: String,
    button: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    DesignedOutlinedTitledBlock(
        title = title,
        trailingContent = {
            TextButton(onClick = onClick) {
                Text(button)
            }
        },
        modifier = modifier,
        content = content
    )
}

@Composable
fun DesignedTitledBlock(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 15.dp)
        )
        Box {
            content()
        }
    }
}

@Composable
fun DesignedTitledBlock(
    title: String,
    button: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            )
            TextButton(onClick = onClick) {
                Text(button)
            }
        }
        Box {
            content()
        }
    }
}
