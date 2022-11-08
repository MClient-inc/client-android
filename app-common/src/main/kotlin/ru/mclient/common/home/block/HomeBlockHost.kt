package ru.mclient.common.home.block

import ru.mclient.common.bar.MergedHost
import ru.mclient.common.record.upcoming.UpcomingRecords
import ru.mclient.common.scanner.Scanner


class HomeBlockState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
)


interface HomeBlockHost : MergedHost {

    val upcomingRecords: UpcomingRecords

    val scanner: Scanner

    fun onRefresh()

    val state: HomeBlockState

    fun onScanner()

}