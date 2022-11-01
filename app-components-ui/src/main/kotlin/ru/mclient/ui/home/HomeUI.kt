package ru.mclient.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.home.Home
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.record.upcoming.RecordsUpcomingUI

@Composable
fun HomeUI(component: Home, modifier: Modifier) {
    TopBarHostUI(component = component) {
        Column(
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