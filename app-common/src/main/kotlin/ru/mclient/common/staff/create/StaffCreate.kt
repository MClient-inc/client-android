package ru.mclient.common.staff.create

data class StaffCreateState(
    val name: String,
    val codename: String,
    val role: String,
    val isLoading: Boolean,
    val isError: Boolean,
    val isButtonsEnabled: Boolean
)

interface StaffCreate {

    val state: StaffCreateState

    fun onUpdate(username: String, codename: String, role: String)

    fun onCreate(username: String, codename: String, role: String)

}