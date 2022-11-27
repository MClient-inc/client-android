package ru.mclient.common.home.block

data class HomeAnalyticsState(
    val analytics: Analytics?,
) {

    data class Analytics(
        val totalSum: Long,
        val averageSum: Long,
        val comeCount: Int,
        val notComeCount: Int,
        val waitingCome: Int,
    )

}

interface HomeAnalytics {

    val state: HomeAnalyticsState

    fun onForceRefresh()

}