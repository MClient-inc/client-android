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
        data class Analytics(
            val totalSum: Long,
            val averageSum: Long,
            val comeCount: Int,
            val notComeCount: Int,
            val waitingCome: Int,
        )

    }

    sealed class Label

}