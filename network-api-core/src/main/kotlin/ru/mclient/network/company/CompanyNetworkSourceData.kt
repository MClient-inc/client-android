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
    val networkId: Long,
)

class GetCompaniesByNetworkInput(
    val networkId: Long,
)

class GetCompaniesByNetworkOutput(
    val companies: List<Company>,
) {
    class Company(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}
class GetCompanyInput(
    val companyId: Long,
)

class GetCompanyOutput(
    val company: Company,
) {
    class Company(
        val id: Long,
        val title: String,
        val codename: String,
        val description: String,
        val icon: String?,
        val networkId: Long,
    )
}
class GetNetworkInput(
    val networkId: Long,
)

class GetNetworkOutput(
    val network: CompanyNetwork,
) {
    class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
        val description: String,
        val icon: String?,
    )
}

class GetCompanyNetworksInput(val accountId: Long)

class GetCompanyNetworksOutput(
    val networks: List<CompanyNetwork>,
) {
    class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

