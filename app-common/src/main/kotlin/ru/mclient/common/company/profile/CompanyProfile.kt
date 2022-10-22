package ru.mclient.common.company.profile

data class CompanyProfileState(
    val profile: Profile?,
    val isLoading: Boolean,
    val isFailure: Boolean,
) {
    data class Profile(
        val title: String,
        val codename: String,
        val description: String,
    )
}

interface CompanyProfile {

    val state: CompanyProfileState

    fun onRefresh()

    fun onEdit()

    fun onClients()

    fun onServices()

    fun onStaff()

    fun onNetwork()

}