package ru.mclient.common.staff.profile

data class StaffProfileState(
    val staff: Staff?,
    val isLoading: Boolean,
) {
    data class Staff(
        val name: String,
        val codename: String,
        val role: String,
    )
}


interface StaffProfile {

    val state: StaffProfileState

    fun onRefresh()

    fun onEdit()

}