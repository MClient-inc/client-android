package ru.mclient.mvi.home

import ru.mclient.mvi.ParametrizedStore

interface HomeAnalyticsStore :
    ParametrizedStore<HomeAnalyticsStore.Intent, HomeAnalyticsStore.State, HomeAnalyticsStore.Label, HomeAnalyticsStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        object Refresh : Intent()

    }

    data class State(
        val analytics: Analytics?,
        val isLoading: Boolean,
        val isFailure: Boolean,
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

    sealed class Label

}