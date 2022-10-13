package ru.mclient.ui.bar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mclient.common.bar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUI(
    component: TopBar,
    modifier: Modifier,
) {
    val state by component.state.collectAsState()
    CenterAlignedTopAppBar(
        title = { Text(text = state.title) },
        modifier = modifier,
    )
}