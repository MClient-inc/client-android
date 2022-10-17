package ru.mclient.common.staff.create

import androidx.compose.runtime.State

data class StaffCreateState(
    val name: String,
    val codename: String,
    val role: String,
    val isLoading: Boolean,
    val isError: Boolean,
    val isButtonsEnabled: Boolean
)

interface StaffCreate {

    val state: State<StaffCreateState>

    fun onUpdate(username: String, codename: String, role: String)

    fun onCreate(username: String, codename: String, role: String)

}