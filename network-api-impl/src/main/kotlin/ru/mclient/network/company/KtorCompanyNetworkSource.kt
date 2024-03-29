package ru.mclient.network.company

//
//class KtorCompanyNetworkSource(
//    @Named("authorized")
//    private val client: HttpClient,
//) : CompanyNetworkSource {
//
//    override suspend fun createCompany(input: CompanyCreateInput): CompanyCreateOutput {
//        val response = client.post("/companies") {
//            setBody(CompanyCreateRequest(input.title, input.codename, input.description))
//        }
//        val body = response.body<CompanyCreateResponse>()
//        return CompanyCreateOutput(
//            title = body.title,
//            codename = body.codename,
//            description = body.description,
//            networkId = body.networkId
//        )
//    }
//
//    override suspend fun getCompanies(input: GetCompaniesByNetworkInput): GetCompaniesByNetworkOutput {
//        val response = client.get("/networks/${input.networkId}/companies")
//        val body = response.body<GetCompanyBranchesForNetworkResponse>()
//        return GetCompaniesByNetworkOutput(
//            companies = body.companies.map { company ->
//                GetCompaniesByNetworkOutput.Company(
//                    id = company.id,
//                    title = company.title,
//                    codename = company.codename,
//                    icon = null,
//                )
//            }
//        )
//    }
//
//    override suspend fun getNetwork(input: GetNetworkInput): GetNetworkOutput {
//        val response = client.get("/networks/${input.networkId}")
//        val body = response.body<GetCompanyNetworkResponse>()
//        return GetNetworkOutput(
//            network = GetNetworkOutput.CompanyNetwork(
//                id = body.id,
//                codename = body.codename,
//                title = body.title,
//                description = "",
//                icon = null,
//            )
//        )
//    }
//
//    override suspend fun getCompany(input: GetCompanyInput): GetCompanyOutput {
//        val response = client.get("/companies/${input.companyId}")
//        val body = response.body<GetCompanyBranchResponse>()
//        return GetCompanyOutput(
//            company = GetCompanyOutput.Company(
//                id = body.id,
//                codename = body.codename,
//                title = body.title,
//                description = "",
//                networkId = body.networkId,
//                icon = null,
//            )
//        )
//    }
//
//    override suspend fun getNetworks(input: GetCompanyNetworksInput): GetCompanyNetworksOutput {
//        val response = client.get("/networks")
//        val body = response.body<GetCompanyNetworksResponse>()
//        return GetCompanyNetworksOutput(
//            networks = body.networks.map { network ->
//                GetCompanyNetworksOutput.CompanyNetwork(
//                    id = network.id,
//                    title = network.title,
//                    codename = network.codename,
//                    icon = null,
//                )
//            }
//        )
//    }
//}
//
//@Serializable
//class CompanyCreateRequest(
//    val title: String,
//    val codename: String,
//    val description: String,
//)
//
//@Serializable
//class CompanyCreateResponse(
//    val id: Long,
//    val networkId: Long,
//    val codename: String,
//    val title: String,
//    val description: String,
//)
//
//
//@Serializable
//class GetCompanyBranchesForNetworkRequest(
//    val networkId: Long,
//)
//
//@Serializable
//class GetCompanyBranchesForNetworkResponse(
//    val networkId: Long,
//    val companies: List<Company>,
//) {
//    @Serializable
//    class Company(
//        val id: Long,
//        val title: String,
//        val codename: String,
//    )
//}
//
//@Serializable
//class GetCompanyNetworksResponse(
//    val networks: List<CompanyNetwork>,
//) {
//    @Serializable
//    class CompanyNetwork(
//        val id: Long,
//        val title: String,
//        val codename: String,
//    )
//}
//
//@Serializable
//class GetCompanyBranchResponse(
//    val id: Long,
//    val title: String,
//    val codename: String,
//    val networkId: Long,
//)
//
//@Serializable
//class GetCompanyNetworkResponse(
//    val id: Long,
//    val title: String,
//    val codename: String,
//)
