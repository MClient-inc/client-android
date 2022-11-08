package ru.mclient.common.record.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.childDIContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.profile.RecordProfileStore

class RecordProfileComponent(
    componentContext: DIComponentContext,
    recordId: Long,
) : RecordProfile, DIComponentContext by componentContext {

    private val store: RecordProfileStore =
        getParameterizedStore { RecordProfileStore.Params(recordId) }

    override val abonements = ClientAbonementsSelectorComponent(
        childDIContext("record_abonements_selector"),
    )

    override val state: RecordProfileState by store.states(this) { it.toState() }

    private fun RecordProfileStore.State.toState(): RecordProfileState {
        if (record?.status == RecordProfileStore.State.RecordVisitStatus.COME)
            abonements.setSelectedAbonements(
                record?.client?.id,
                store.state.record?.abonements?.map { it.id })
        else
            abonements.setSelectedAbonements(null, null)
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
            id = id,
            status = when (status) {
                RecordProfileStore.State.RecordVisitStatus.WAITING -> RecordProfileState.RecordStatus.WAITING
                RecordProfileStore.State.RecordVisitStatus.COME -> RecordProfileState.RecordStatus.COME
                RecordProfileStore.State.RecordVisitStatus.NOT_COME -> RecordProfileState.RecordStatus.NOT_COME
            }
        )
    }

    override fun onCome() {
        store.accept(RecordProfileStore.Intent.Come)
    }

    override fun onNotCome() {
        store.accept(RecordProfileStore.Intent.NotCome)
    }

    override fun onWaiting() {
        store.accept(RecordProfileStore.Intent.Waiting)
    }

    override fun onRefresh() {
        store.accept(RecordProfileStore.Intent.Refresh)
    }

    override fun onApplyAbonements() {
        store.accept(RecordProfileStore.Intent.UseAbonements(abonements.state.selectedAbonements.map { it.value.id }))
    }

}