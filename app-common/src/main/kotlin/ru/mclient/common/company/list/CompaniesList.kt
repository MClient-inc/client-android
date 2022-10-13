package ru.mclient.common.company.list

import kotlinx.coroutines.flow.StateFlow

data class CompaniesListState(
    val companies: List<Company>,
    val isLoading: Boolean,
) {
    data class Company(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

interface CompaniesList {

    val state: StateFlow<CompaniesListState>

    fun onRefresh()

    fun onSelect(company: CompaniesListState.Company)

}