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
                    totalSum = it.totalSum,
                    averageSum = it.averageSum,
                    comeCount = it.comeCount,
                    notComeCount = it.notComeCount,
                    waitingCome = it.waitingCome,
                )
            }
        )
    }

    override fun onForceRefresh() {
        store.accept(HomeAnalyticsStore.Intent.Refresh)
    }
}