package ru.mclient.ui.storage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.storage.Storage

@Composable
fun StorageUI(component: Storage, modifier: Modifier) {
    StoragePage(modifier = modifier)
}