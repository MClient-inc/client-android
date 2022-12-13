package ru.mclient.common.staff.schedule

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.schedule.StaffScheduleEditStore
import java.time.LocalDate

class StaffScheduleComponent(
    componentContext: DIComponentContext,
    staffId: Long,
    companyId: Long,
    private val onSuccess: () -> Unit,
) : StaffSchedule, DIComponentContext by componentContext {

    private val store: StaffScheduleEditStore =
        getParameterizedStore { StaffScheduleEditStore.Params(staffId, companyId) }

    override val state: StaffScheduleState by store.states(this) { it.toState() }

    private fun StaffScheduleEditStore.State.toState(): StaffScheduleState {
        if (isSuccess)
            onSuccess()
        return StaffScheduleState(
            selectedSchedule = selectedSchedule.map {
                StaffScheduleState.Schedule(
                    start = it.start,
                    end = it.end,
                )
            },
            isLoading = isLoading,
            isAvailable = isAvailable
        )
    }

    override fun onSelect(start: LocalDate, end: LocalDate) {
        store.accept(StaffScheduleEditStore.Intent.Select(start, end))
    }

    override fun onSelect(date: LocalDate) {
        onSelect(date, date)
    }

    override fun onContinue() {
        store.accept(StaffScheduleEditStore.Intent.Apply)
    }

}