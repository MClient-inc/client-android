package ru.mclient.common.staff.list

data class StaffListState(
    val staff: List<Staff>,
    val isLoading: Boolean,
) {
    data class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
        val icon: String?,
    )
}

interface StaffList {

    val state: StaffListState

    fun onRefresh()

    fun onSelect(staffId: Long, staffName: String)

}