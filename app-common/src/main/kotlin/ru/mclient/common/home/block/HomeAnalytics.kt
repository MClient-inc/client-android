package ru.mclient.common.home.block

data class HomeAnalyticsState(
    val analytics: Analytics?,
) {


    class AnalyticItem(
        val value: String,
        val difference: Int,
    )

    class Analytics(
        val totalSum: AnalyticItem,
        val averageSum: AnalyticItem,
        val comeCount: AnalyticItem,
        val notComeCount: AnalyticItem,
        val waitingCount: AnalyticItem,
    )
}

interface HomeAnalytics {

    val state: HomeAnalyticsState

    fun onForceRefresh()

}