package ru.mclient.common.home

import ru.mclient.common.bar.TopBarHost
import ru.mclient.common.record.upcoming.UpcomingRecords

class HomeState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
)

interface Home : TopBarHost {

    val upcomingRecords: UpcomingRecords

    fun onRefresh()
    val state: HomeState
}