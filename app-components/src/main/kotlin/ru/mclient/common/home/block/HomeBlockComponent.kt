package ru.mclient.common.home.block

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.MutableTopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.record.upcoming.UpcomingRecords
import ru.mclient.common.record.upcoming.UpcomingRecordsComponent
import ru.mclient.common.record.upcoming.UpcomingRecordsState
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.home.HomeStore

class HomeBlockComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelectRecord: (Long) -> Unit,
    onRecordsList: () -> Unit,
) : HomeBlockHost, DIComponentContext by componentContext {

    private val store: HomeStore = getParameterizedStore { HomeStore.Params(companyId) }

    override val bar: MutableTopBar = MutableTopBar(
        state = store.state.toTopBarState()
    )

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
        UpcomingRecordsComponent(componentContext, companyId, onSelectRecord, onRecordsList)

    override val state: HomeBlockState get() = mergeState(upcomingRecords.state)

    private fun mergeState(upcomingRecordsState: UpcomingRecordsState): HomeBlockState {
        return HomeBlockState(
            isLoading = upcomingRecordsState.isLoading,
            isRefreshing = upcomingRecordsState.isRefreshing,
        )
    }

    override fun onRefresh() {
        upcomingRecords.onRefresh()
    }
}