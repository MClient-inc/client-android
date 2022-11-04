package ru.mclient.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.home.block.HomeBlockHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.record.upcoming.RecordsUpcomingUI
import ru.mclient.ui.view.DesignedRefreshColumn

@Composable
fun HomeBlockHostUI(
    component: HomeBlockHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component) {
        DesignedRefreshColumn(
            refreshing = component.state.isRefreshing,
            onRefresh = component::onRefresh,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            RecordsUpcomingUI(
                component = component.upcomingRecords,
                modifier = modifier,
            )
        }
    }
}