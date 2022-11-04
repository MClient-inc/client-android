package ru.mclient.common.record.create

import ru.mclient.common.staff.list.StaffList

data class RecordCreateStaffSelectorState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val isSuccess: Boolean,
    val selectedStaff: Staff?,
) {
    class Staff(
        val id: Long,
        val name: String,
    )
}

interface RecordCreateStaffSelector {

    val state: RecordCreateStaffSelectorState

    val staffList: StaffList

    fun onDismiss()

    fun onExpand()

}