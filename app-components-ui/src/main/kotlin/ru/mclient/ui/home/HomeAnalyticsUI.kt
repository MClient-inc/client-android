package ru.mclient.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.home.block.HomeAnalytics
import ru.mclient.common.home.block.HomeAnalyticsState
import ru.mclient.ui.analytics.home.HomeAnalyticsBlock
import ru.mclient.ui.analytics.home.HomeAnalyticsBlockState

@Composable
fun HomeAnalyticsUI(component: HomeAnalytics, modifier: Modifier) {
    HomeAnalyticsBlock(state = component.state.toUI(), modifier = modifier)
}

private fun HomeAnalyticsState.toUI(): HomeAnalyticsBlockState {
    return HomeAnalyticsBlockState(
        analytics = analytics?.let {
            HomeAnalyticsBlockState.Analytics(
                totalSum = it.totalSum,
                averageSum = it.averageSum,
                comeCount = it.comeCount,
                notComeCount = it.notComeCount,
                waitingCount = it.waitingCome,
            )
        }
    )
}
