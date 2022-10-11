package ru.mclient.ui.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignedRefreshColumn(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    Box(modifier = Modifier.pullRefresh(refreshState)) {
        DesignedIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
        val columnOffset by animateDpAsState(getRefreshOffset(refreshing, refreshState.progress))
        Column(
            modifier = modifier.offset(y = columnOffset),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }

}