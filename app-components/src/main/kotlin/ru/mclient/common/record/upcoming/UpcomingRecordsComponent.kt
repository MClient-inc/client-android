package ru.mclient.common.record.upcoming

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.company.record.UpcomingRecordsStore

class UpcomingRecordsComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelect: (Long) -> Unit,
) : UpcomingRecords, DIComponentContext by componentContext {


    private val store: UpcomingRecordsStore =
        getParameterizedStore { UpcomingRecordsStore.Params(companyId = companyId) }

    override val state: UpcomingRecordsState by store.states(this) { it.toState() }

    private fun UpcomingRecordsStore.State.toState(): UpcomingRecordsState {
        return UpcomingRecordsState(
            records = records.map { it.toState() },
            isLoading = isLoading,
            isFailure = isFailure,
            isRefreshing = records.isNotEmpty() && isLoading,
        )
    }

    private fun UpcomingRecordsStore.State.Record.toState(): UpcomingRecordsState.Record {
        return UpcomingRecordsState.Record(
            id = id,
            schedule = UpcomingRecordsState.Schedule(
                staff = UpcomingRecordsState.Staff(
                    id = schedule.staff.id,
                    name = schedule.staff.name
                ),
                date = schedule.date,
            ),
            time = UpcomingRecordsState.TimeOffset(start = time.start, end = time.end),
            client = UpcomingRecordsState.Client(
                id = client.id,
                name = client.name,
                phone = client.phone,
            ),
            services = services.map { service ->
                UpcomingRecordsState.Service(
                    id = service.id,
                    title = service.title,
                )
            }
        )
    }


    override fun onRefresh() {
        store.accept(UpcomingRecordsStore.Intent.Refresh)
    }

    override fun onSelect(recordId: Long) {
        TODO("Not yet implemented")
    }


}