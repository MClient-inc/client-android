package ru.mclient.common.fab

data class FabState(
    val title: String,
    val isShown: Boolean = true,
    val isScrollInProgress: Boolean = false,
)

interface Fab {

    val state: FabState

    fun onClick()

}