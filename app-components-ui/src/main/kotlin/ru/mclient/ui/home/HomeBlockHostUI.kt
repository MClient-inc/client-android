package ru.mclient.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.home.block.HomeBlockHost
import ru.mclient.ui.bar.MergedHostUI
import ru.mclient.ui.record.upcoming.RecordsUpcomingUI
import ru.mclient.ui.scanner.ScannerUI
import ru.mclient.ui.view.DesignedRefreshColumn

@Composable
fun HomeBlockHostUI(
    component: HomeBlockHost,
    modifier: Modifier,
) {
    ScannerUI(
        component = component.scanner,
        modifier = modifier,
    ) {
        MergedHostUI(component = component, modifier = Modifier.fillMaxSize()) {
            DesignedRefreshColumn(
                refreshing = component.state.isRefreshing,
                onRefresh = component::onRefresh,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
            ) {
                RecordsUpcomingUI(
                    component = component.upcomingRecords,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}