package ru.mclient.common.staff.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.staff.list.StaffListForCompanyAndScheduleStore
import java.time.LocalDateTime

class StaffListForCompanyAndScheduleComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    schedule: LocalDateTime?,
    private val onSelect: (Long, String) -> Unit,
) : StaffList, DIComponentContext by componentContext {

    private val store: StaffListForCompanyAndScheduleStore =
        getParameterizedStore { StaffListForCompanyAndScheduleStore.Params(companyId, schedule) }

    override val state: StaffListState by store.states(this) { it.toState() }

    private fun StaffListForCompanyAndScheduleStore.State.toState(): StaffListState {
        return StaffListState(
            staff = staff.map {
                StaffListState.Staff(
                    id = it.id,
                    name = it.name,
                    codename = it.codename,
                    icon = it.icon,
                    role = it.role,
                )
            },
            isLoading = isLoading,
        )
    }

    fun onNewSchedule(schedule: LocalDateTime?) {
        store.accept(StaffListForCompanyAndScheduleStore.Intent.RefreshSchedule(schedule))
    }


    override fun onRefresh() {
        store.accept(StaffListForCompanyAndScheduleStore.Intent.Refresh)
    }

    override fun onSelect(staffId: Long, staffName: String) {
        onSelect.invoke(staffId, staffName)
    }


}