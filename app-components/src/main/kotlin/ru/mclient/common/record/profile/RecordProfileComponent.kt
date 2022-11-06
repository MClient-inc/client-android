package ru.mclient.common.record.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.profile.RecordProfileStore

class RecordProfileComponent(
    componentContext: DIComponentContext,
    recordId: Long
) : RecordProfile, DIComponentContext by componentContext {

    private val store: RecordProfileStore =
        getParameterizedStore { RecordProfileStore.Params(recordId) }

    override val state: RecordProfileState by store.states(this) { it.toState() }

    private fun RecordProfileStore.State.toState(): RecordProfileState {
        return RecordProfileState(
            record = record?.toState(),
            isLoading = isLoading,
            isRefreshing = isRefreshing,
        )
    }

    private fun RecordProfileStore.State.Record.toState(): RecordProfileState.Record {
        return RecordProfileState.Record(
            schedule = RecordProfileState.Schedule(
                date = schedule.date,
                start = schedule.start,
                end = schedule.end,
                id = schedule.id
            ),
            services = services.map { s ->
                RecordProfileState.Service(
                    id = s.id,
                    cost = s.cost,
                    title = s.title,
                    description = s.description
                )
            },
            staff = RecordProfileState.Staff(
                role = staff.role,
                id = staff.id,
                name = staff.name,
                codename = staff.codename
            ),
            totalCost = totalCost,
            time = RecordProfileState.TimeOffset(
                start = time.start,
                end = time.end
            ),
            client = RecordProfileState.Client(
                id = client.id,
                phone = client.phone,
                name = client.name
            ),
            id = id
        )
    }

    override fun onRefresh() {
        TODO("Not yet implemented")
    }

}