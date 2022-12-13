package ru.mclient.network.company

class CompanyCreateInput(
    val title: String,
    val codename: String,
    val description: String,
)

class CompanyCreateOutput(
    val title: String,
    val codename: String,
    val description: String,
    val networkId: String,
)

class GetCompaniesByNetworkInput(
    val networkId: String,
)

class GetCompaniesByNetworkOutput(
    val companies: List<Company>,
) {
    class Company(
        val id: String,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}
class GetCompanyInput(
    val companyId: String,
)

class GetCompanyOutput(
    val company: Company,
) {
    class Company(
        val id: String,
        val title: String,
        val codename: String,
        val description: String,
        val icon: String?,
        val networkId: String,
    )
}
class GetNetworkInput(
    val networkId: String,
)

class GetNetworkOutput(
    val network: CompanyNetwork,
) {
    class CompanyNetwork(
        val id: String,
        val title: String,
        val codename: String,
        val description: String,
        val icon: String?,
    )
}

class GetCompanyNetworksInput(val accountId: String)

class GetCompanyNetworksOutput(
    val networks: List<CompanyNetwork>,
) {
    class CompanyNetwork(
        val id: String,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

