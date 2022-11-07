package ru.mclient.common.record.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.childDIContext
import ru.mclient.common.staff.list.StaffListForCompanyAndScheduleComponent
import ru.mclient.common.utils.getSavedStateStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateStaffSelectorStore
import java.time.LocalDateTime

class RecordCreateStaffSelectorComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : RecordCreateStaffSelector, DIComponentContext by componentContext {

    private val store: RecordCreateStaffSelectorStore = getSavedStateStore("record_create_staff")

    override val state: RecordCreateStaffSelectorState by store.states(this) { it.toState() }

    private fun RecordCreateStaffSelectorStore.State.toState(): RecordCreateStaffSelectorState {
        return RecordCreateStaffSelectorState(
            isExpanded = isExpanded,
            isAvailable = isAvailable,
            isSuccess = selectedStaff != null,
            selectedStaff = selectedStaff?.let {
                RecordCreateStaffSelectorState.Staff(
                    id = it.id,
                    name = it.name
                )
            },
        )
    }

    override val staffList = StaffListForCompanyAndScheduleComponent(
        componentContext = childDIContext(key = "staff_list"),
        companyId = companyId,
        schedule = store.state.schedule,
        onSelect = this::onSelect,
    )

    fun onChangeSchedule(schedule: LocalDateTime?) {
        store.accept(RecordCreateStaffSelectorStore.Intent.ChangeSchedule(schedule))
        staffList.onNewSchedule(schedule)
    }

    private fun onSelect(staffId: Long, staffName: String) {
        store.accept(RecordCreateStaffSelectorStore.Intent.Select(staffId, staffName))
    }

    override fun onDismiss() {
        store.accept(RecordCreateStaffSelectorStore.Intent.Move(false))
    }

    override fun onExpand() {
        store.accept(RecordCreateStaffSelectorStore.Intent.Move(true))
    }


}