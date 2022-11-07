package ru.mclient.common.record.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.list.RecordsListStore

class RecordsListComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSelect:(RecordsListState.Record) -> Unit,
) : RecordsList, DIComponentContext by componentContext {

    private val store: RecordsListStore =
        getParameterizedStore { RecordsListStore.Params(companyId) }

    override val state: RecordsListState by store.states(this) { it.toState() }

    private fun RecordsListStore.State.toState(): RecordsListState {
        return RecordsListState(
            records = records.map { record ->
                RecordsListState.Record(
                    id = record.id,
                    client = RecordsListState.Client(
                        name = record.client.name,
                        phone = record.client.phone,
                        formattedPhone = record.client.formattedPhone,
                    ),
                    staff = RecordsListState.Staff(record.schedule.staff.name),
                    services = record.services.map { service ->
                        RecordsListState.Service(service.title, service.cost)
                    },
                    formattedCost = record.formattedCost,
                    formattedTime = record.formattedTime,
                    cost = record.cost,
                )
            },
            isLoading = isLoading,
            isRefreshing = records.isNotEmpty() && isLoading,
            isFailure = isFailure,
        )
    }

    override fun onRefresh() {
        store.accept(RecordsListStore.Intent.Refresh)
    }

    override fun onSelect(recordId: Long) {
        state.records.find { it.id == recordId }?.let { onSelect.invoke(it) }
    }
}