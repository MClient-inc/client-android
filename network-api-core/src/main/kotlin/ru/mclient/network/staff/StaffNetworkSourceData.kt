package ru.mclient.network.staff

data class GetStaffForCompanyInput(
    val companyId: Long
)

data class GetStaffForCompanyOutput(
    val staff: List<Staff>
) {
    data class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val icon: String,
    )
}