package ru.mclient.network.company

import com.apollographql.apollo3.ApolloClient
import org.koin.core.annotation.Single
import ru.mclient.network.data.GetAccountNetworksQuery
import ru.mclient.network.data.GetCompanyQuery
import ru.mclient.network.data.GetNetworkCompaniesQuery
import ru.mclient.network.data.GetNetworkQuery


@Single
class GraphQLCompanyNetworkSource(
    private val client: ApolloClient,
) : CompanyNetworkSource {

    override suspend fun createCompany(input: CompanyCreateInput): CompanyCreateOutput {
        TODO("Not yet implemented")
    }

    override suspend fun getCompanies(input: GetCompaniesByNetworkInput): GetCompaniesByNetworkOutput {
        val response = client.query(GetNetworkCompaniesQuery(input.networkId)).execute()
        return GetCompaniesByNetworkOutput(
            response.dataAssertNoErrors.network.companies.map {
                it.baseCompany.let { company ->
                    GetCompaniesByNetworkOutput.Company(
                        id = company.id,
                        title = company.data.title,
                        codename = company.codename,
                        icon = null,
                    )
                }
            }
        )
    }

    override suspend fun getCompany(input: GetCompanyInput): GetCompanyOutput {
        val response = client.query(GetCompanyQuery(input.companyId)).execute()
        return GetCompanyOutput(
            response.dataAssertNoErrors.company.baseCompany.let { company ->
                GetCompanyOutput.Company(
                    id = company.id,
                    title = company.data.title,
                    codename = company.codename,
                    icon = null,
                    description = company.data.description.orEmpty(),
                    networkId = company.network.id,
                )
            },
        )
    }

    override suspend fun getNetwork(input: GetNetworkInput): GetNetworkOutput {
        val response = client.query(GetNetworkQuery(input.networkId)).execute()
        return GetNetworkOutput(
            response.dataAssertNoErrors.network.baseNetwork.let { network ->
                GetNetworkOutput.CompanyNetwork(
                    id = network.id,
                    title = network.data.title,
                    codename = network.codename,
                    description = network.data.description.orEmpty(),
                    icon = null,
                )
            }
        )
    }

    override suspend fun getNetworks(input: GetCompanyNetworksInput): GetCompanyNetworksOutput {
        val response = client.query(GetAccountNetworksQuery()).execute()
        return GetCompanyNetworksOutput(
            response.dataAssertNoErrors.account.networks.map {
                it.baseNetwork.let { network ->
                    GetCompanyNetworksOutput.CompanyNetwork(
                        id = network.id,
                        title = network.data.title,
                        codename = network.codename,
                        icon = null,
                    )
                }
            }
        )
    }


}