package ru.mclient.common.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

open class ImmutableTopBar(
    state: TopBarState,
) : TopBar {

    override val state: TopBarState by mutableStateOf(state)

}