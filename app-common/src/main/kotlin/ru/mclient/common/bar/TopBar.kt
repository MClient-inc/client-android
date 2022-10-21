package ru.mclient.common.bar

data class TopBarState(
    val title: String,
    val isLoading: Boolean = false,
)

interface TopBar {

    val state: TopBarState

}