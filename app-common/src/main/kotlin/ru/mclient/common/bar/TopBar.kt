package ru.mclient.common.bar

import kotlinx.coroutines.flow.StateFlow

data class TopBarState(
    val title: String,
)

interface TopBar {

    val state: StateFlow<TopBarState>

}