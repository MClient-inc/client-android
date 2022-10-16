package ru.mclient.common.staff.list

import kotlinx.coroutines.flow.StateFlow

data class StaffListState(
    val staff: List<Staff>,
    val isLoading: Boolean,
) {
    data class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val icon: String?,
    )
}

interface StaffList {

    val state: StateFlow<StaffListState>

    fun onRefresh()

    fun onSelect(staffId: Long)

    fun onCreateStaff()
}