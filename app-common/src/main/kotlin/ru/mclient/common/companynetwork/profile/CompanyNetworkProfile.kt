package ru.mclient.common.companynetwork.profile

import androidx.compose.runtime.State

data class CompanyNetworkProfileState(
    val network: CompanyNetwork?,
    val isLoading: Boolean,
    val isFailure: Boolean,
) {
    data class CompanyNetwork(
        val title: String,
        val codename: String,
        val description: String,
    )
}

interface CompanyNetworkProfile {

    val state: State<CompanyNetworkProfileState>

    fun onRefresh()

    fun onEdit()

    fun onClients()

    fun onServices()

    fun onStaff()

    fun onCompany()

    fun onAnalytics()

}