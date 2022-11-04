package ru.mclient.common.fab

data class FabState(
    val title: String,
    val isShown: Boolean,
    val isScrollInProgress: Boolean,
)

interface Fab {

    val state: FabState

    fun onClick()

}