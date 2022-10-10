package ru.mclient.common.company.staff.create

import kotlinx.coroutines.flow.StateFlow

data class StaffCreateState(
    val username: String,
    val codename: String,
    val isLoading: Boolean,
    val isError: Boolean
)

interface StaffCreate {
    val state : StateFlow<StaffCreateState>

    fun onUpdate(username: String, codename: String)

    fun onCreate(username: String, codename: String)
}