package ru.mclient.common.home.block

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.home.HomeAnalyticsStore

class HomeAnalyticsComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : HomeAnalytics, DIComponentContext by componentContext {


    private val store: HomeAnalyticsStore =
        getParameterizedStore { HomeAnalyticsStore.Params(companyId) }

    override val state: HomeAnalyticsState by store.states(this) { it.toState() }

    private fun HomeAnalyticsStore.State.toState(): HomeAnalyticsState {
        return HomeAnalyticsState(
            analytics = analytics?.let {
                HomeAnalyticsState.Analytics(
                    totalSum = it.totalSum.toItem(),
                    averageSum = it.averageSum.toItem(),
                    comeCount = it.comeCount.toItem(),
                    notComeCount = it.notComeCount.toItem(),
                    waitingCount = it.waitingCount.toItem(),
                )
            }
        )
    }

    override fun onForceRefresh() {
        store.accept(HomeAnalyticsStore.Intent.Refresh)
    }
}

private fun HomeAnalyticsStore.State.AnalyticItem.toItem(): HomeAnalyticsState.AnalyticItem {
    return HomeAnalyticsState.AnalyticItem(value, difference)
}
