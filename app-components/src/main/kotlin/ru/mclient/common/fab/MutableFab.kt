package ru.mclient.common.fab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MutableFab(
    state: FabState,
    onClick: () -> Unit,
) : ImmutableFab(state, onClick) {

    override var state: FabState by mutableStateOf(state)
        private set

    fun update(function: (FabState) -> FabState) {
        this.state = function(state)
    }

}