package ru.mclient.common.companynetwork.profile

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

    val state: CompanyNetworkProfileState

    fun onRefresh()

    fun onEdit()

    fun onClients()

    fun onServices()

    fun onStaff()

    fun onCompany()

    fun onAnalytics()

}