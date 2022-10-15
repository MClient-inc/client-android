package ru.mclient.common.staff.profile

import kotlinx.coroutines.flow.StateFlow

data class StaffProfileState(
    val staff: Staff?,
    val isLoading: Boolean,
) {
    data class Staff(
        val name: String,
        val codename: String,
    )
}


interface StaffProfile {

    val state: StateFlow<StaffProfileState>

    fun onRefresh()

    fun onEdit()

}