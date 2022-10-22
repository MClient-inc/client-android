package ru.mclient.common.company.list

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

    val state: CompaniesListState

    fun onRefresh()

    fun onSelect(companyId: Long)

}