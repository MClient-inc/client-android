package ru.mclient.network.company

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class KtorCompanyNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : CompanyNetworkSource {

    override suspend fun createCompany(input: CompanyCreateInput): CompanyCreateOutput {
        val response = client.get("/companies") {
            setBody(CompanyCreateRequest(input.title, input.codename, input.description))
        }
        val body = response.body<CompanyCreateResponse>()
        return CompanyCreateOutput(
            title = body.title,
            codename = body.codename,
            description = body.description,
            networkId = body.networkId
        )
    }

    override suspend fun getCompanies(input: GetCompaniesByNetworkInput): GetCompaniesByNetworkOutput {
        val response = client.get("/networks/${input.networkId}/companies")
        val body = response.body<GetCompanyBranchesForNetworkResponse>()
        return GetCompaniesByNetworkOutput(
            companies = body.companies.map { company ->
                GetCompaniesByNetworkOutput.Company(
                    id = company.id,
                    title = company.title,
                    codename = company.codename,
                    icon = null,
                )
            }
        )
    }

    override suspend fun getNetworks(input: GetCompanyNetworksInput): GetCompanyNetworksOutput {
        val response = client.get("/networks")
        val body = response.body<GetCompanyNetworksResponse>()
        return GetCompanyNetworksOutput(
            networks = body.networks.map { network ->
                GetCompanyNetworksOutput.CompanyNetwork(
                    id = network.id,
                    title = network.title,
                    codename = network.codename,
                    icon = null,
                )
            }
        )
    }
}

@Serializable
class CompanyCreateRequest(
    val title: String,
    val codename: String,
    val description: String,
)

@Serializable
class CompanyCreateResponse(
    val id: Long,
    val networkId: Long,
    val codename: String,
    val title: String,
    val description: String,
)


@Serializable
class GetCompanyBranchesForNetworkRequest(
    val networkId: Long,
)

@Serializable
class GetCompanyBranchesForNetworkResponse(
    val networkId: Long,
    val companies: List<Company>,
) {
    @Serializable
    class Company(
        val id: Long,
        val title: String,
        val codename: String,
    )
}
@Serializable
class GetCompanyNetworksResponse(
    val networks: List<CompanyNetwork>,
) {
    @Serializable
    class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
    )
}