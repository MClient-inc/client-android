package ru.mclient.common.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MutableTopBar(
    state: TopBarState,
) : ImmutableTopBar(state) {

    override var state: TopBarState by mutableStateOf(state)
        private set

    fun updateState(state: TopBarState) {
        this.state = state
    }

}