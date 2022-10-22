package ru.mclient.common.companynetwork.list


data class CompanyNetworksListState(
    val networks: List<CompanyNetwork>,
    val isLoading: Boolean,
) {
    data class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

interface CompanyNetworksList {

    val state: CompanyNetworksListState

    fun onRefresh()

    fun onSelect(network: CompanyNetworksListState.CompanyNetwork)

}