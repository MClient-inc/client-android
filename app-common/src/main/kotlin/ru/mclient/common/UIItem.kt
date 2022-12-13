package ru.mclient.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface UIItem {

    /**
     * Use only to pass component inside component's ui function. Can't contain any logic
     */
    @Composable
    fun Content(modifier: Modifier)

}