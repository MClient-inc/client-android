package ru.mclient.common.home.block

import ru.mclient.common.bar.TopBarHost
import ru.mclient.common.record.upcoming.UpcomingRecords


class HomeBlockState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
)


interface HomeBlockHost : TopBarHost {

    val upcomingRecords: UpcomingRecords

    fun onRefresh()

    val state: HomeBlockState

}