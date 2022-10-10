package ru.mclient.common.bar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImmutableTopBar(
    state: TopBarState,
) : TopBar {

    override val state: StateFlow<TopBarState> = MutableStateFlow(state)

}