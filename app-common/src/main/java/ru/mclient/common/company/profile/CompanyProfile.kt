package ru.mclient.common.company.profile

import kotlinx.coroutines.flow.StateFlow

data class CompanyProfileState(
    val profile: Profile?,
    val isLoading: Boolean,
) {
    data class Profile(
        val title: String,
        val codename: String,
        val description: String,
    )
}

interface CompanyProfile {

    val state: StateFlow<CompanyProfileState>

    fun onRefresh()

    fun onEdit()

    fun onClients()

    fun onServices()

    fun onStaff()

    fun onNetwork()

}