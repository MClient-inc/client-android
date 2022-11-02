package ru.mclient.common.home

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.MutableTopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.record.upcoming.UpcomingRecords
import ru.mclient.common.record.upcoming.UpcomingRecordsComponent
import ru.mclient.common.record.upcoming.UpcomingRecordsState
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.home.HomeStore

class HomeComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : Home, DIComponentContext by componentContext {

    private val store: HomeStore = getParameterizedStore { HomeStore.Params(companyId) }

    override val bar: MutableTopBar = MutableTopBar(
        state = store.state.toTopBarState()
    )

    private fun onSelectRecord(recordId: Long) {

    }

    init {
        store.states(this) { state ->
            bar.update { state.toTopBarState() }
        }
    }

    private fun HomeStore.State.toTopBarState(): TopBarState {
        return TopBarState(
            title = company?.title ?: "**********",
            isLoading = isFirstLoading,
        )
    }

    override val upcomingRecords: UpcomingRecords =
        UpcomingRecordsComponent(componentContext, companyId, ::onSelectRecord)

    override val state: HomeState get() = mergeState(upcomingRecords.state)

    private fun mergeState(upcomingRecordsState: UpcomingRecordsState): HomeState {
        return HomeState(
            isLoading = upcomingRecordsState.isLoading,
            isRefreshing = upcomingRecordsState.isRefreshing,
        )
    }

    override fun onRefresh() {
        upcomingRecords.onRefresh()
    }


}