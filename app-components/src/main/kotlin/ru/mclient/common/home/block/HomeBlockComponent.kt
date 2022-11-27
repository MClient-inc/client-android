package ru.mclient.common.home.block

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.MutableTopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab
import ru.mclient.common.record.upcoming.UpcomingRecordsComponent
import ru.mclient.common.record.upcoming.UpcomingRecordsState
import ru.mclient.common.scanner.Scanner
import ru.mclient.common.scanner.ScannerComponent
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.home.HomeStore

class HomeBlockComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelectRecord: (Long) -> Unit,
    onRecordsList: () -> Unit,
    onClient: (Long) -> Unit,
) : HomeBlockHost, DIComponentContext by componentContext {

    private val store: HomeStore = getParameterizedStore { HomeStore.Params(companyId) }

    override val analytics: HomeAnalytics =
        HomeAnalyticsComponent(childDIContext("daily_analytics"), companyId = companyId)

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


    override val scanner: Scanner = ScannerComponent(onClient)

    override val upcomingRecords =
        UpcomingRecordsComponent(
            componentContext,
            companyId,
            onSelectRecord,
            onRecordsList,
            onRefresh = analytics::onForceRefresh
        )

    override val state: HomeBlockState get() = mergeState(upcomingRecords.state)

    private fun mergeState(upcomingRecordsState: UpcomingRecordsState): HomeBlockState {
        return HomeBlockState(
            isLoading = upcomingRecordsState.isLoading,
            isRefreshing = upcomingRecordsState.isRefreshing,
        )
    }

    override fun onRefresh() {
        upcomingRecords.onForceRefresh()
        analytics.onForceRefresh()
    }

    override val fab: Fab = ImmutableFab(
        state = FabState("Сканировать", isIconShown = false),
        onClick = ::onScanner,
    )

    override fun onScanner() {
        scanner.updateState(true)
    }

}