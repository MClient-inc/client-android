@file:Suppress("FunctionName")

package ru.mclient.ui.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun DesignedLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignedLazyColumn(
    refreshing: Boolean,
    enabled: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = Modifier
            .pullRefresh(
                state = refreshState,
                enabled = enabled,
            ),
    ) {
        DesignedIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
        val columnOffset by animateDpAsState(getRefreshOffset(refreshing, refreshState.progress))
        DesignedLazyColumn(
            modifier = modifier.offset(y = columnOffset),
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content
        )

    }
}


fun LazyListScope.DefaultLoadingContent() {
    item {
        Box(
            modifier = Modifier.fillParentMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Идёт загрузка...")
        }
    }
}

fun LazyListScope.DefaultEmptyContent(onRefresh: () -> Unit) {
    item {
        Column(
            modifier = Modifier.fillParentMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Пусто...")
            DesignedButton(text = "Обновить".toDesignedString(), onClick = onRefresh)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignedLazyColumn(
    refreshing: Boolean,
    enabled: Boolean,
    onRefresh: () -> Unit,
    loading: Boolean,
    empty: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    emptyContent: LazyListScope.() -> Unit = { DefaultEmptyContent(onRefresh) },
    loadingContent: LazyListScope.() -> Unit = LazyListScope::DefaultLoadingContent,
    content: LazyListScope.() -> Unit,
) {
    DesignedLazyColumn(
        refreshing,
        enabled,
        onRefresh,
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        userScrollEnabled,
        content = {
            when {
                loading && empty -> {
                    loadingContent()
                }

                !loading && empty -> {
                    emptyContent()
                }

                else -> {
                    content()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignedIndicator(refreshing: Boolean, refreshState: PullRefreshState, modifier: Modifier) {
    val progress = minOf(1.4f, refreshState.progress)
    val indicatorOffset = animateDpAsState(getRefreshProgressOffset(refreshing, progress))
    val indicatorAlpha =
        animateFloatAsState(getIndicatorAlpha(refreshing, FastOutSlowInEasing.transform(progress)))
    val scale = animateFloatAsState(getRefreshProgressScale(refreshing, progress))
    if (refreshing) {
        CircularProgressIndicator(
            modifier = modifier
                .offset(y = indicatorOffset.value)
        )
    } else {
        CircularProgressIndicator(
            progress = refreshState.progress,
            modifier = modifier
                .offset(y = indicatorOffset.value)
                .alpha(indicatorAlpha.value)
                .scale(scale.value)
        )
    }
}

fun getRefreshOffset(refreshing: Boolean, progress: Float): Dp {
    if (progress == 0f && refreshing) {
        return 60.dp
    }
    return 70.dp * progress
}


private fun getRefreshProgressOffset(refreshing: Boolean, progress: Float): Dp {
    if (progress == 0f && refreshing) {
        return 5.dp
    }
    return (40.dp * progress) - 30.dp
}

private fun getRefreshProgressScale(refreshing: Boolean, progress: Float): Float {
    if (progress == 0f && refreshing) {
        return 1f
    }
    return (0.75f * progress).coerceIn(0.75f, 1f)
}


private fun getIndicatorAlpha(refreshing: Boolean, progress: Float): Float {
    if (progress == 0f && refreshing) {
        return 1f
    }
    return (progress).coerceIn(0f, 1f)
}


