package ru.mclient.network.servicecategory

import com.apollographql.apollo3.ApolloClient
import org.koin.core.annotation.Single
import ru.mclient.network.data.AddServiceCategoryMutation
import ru.mclient.network.data.ServiceCategoriesByCompanyQuery
import ru.mclient.network.data.ServiceCategoryByIdQuery

@Single
class GraphQLServiceCategoryNetworkSource(
    private val client: ApolloClient,
) : ServiceCategoryNetworkSource {

    override suspend fun getServiceCategoriesByCompany(input: GetServiceCategoriesByCompanyInput): GetServiceCategoriesByCompanyOutput {
        val response = client.query(ServiceCategoriesByCompanyQuery(input.companyId)).execute()
        return GetServiceCategoriesByCompanyOutput(
            response.dataAssertNoErrors.company.serviceCategories.map {
                GetServiceCategoriesByCompanyOutput.ServiceCategory(
                    id = it.id,
                    title = it.data.title
                )
            }
        )
    }

    override suspend fun getServiceCategoryById(input: GetServiceCategoryByIdInput): GetServiceCategoryByIdOutput {
        val response = client.query(ServiceCategoryByIdQuery(input.categoryId)).execute()
        val data = response.dataAssertNoErrors.serviceCategory
        return GetServiceCategoryByIdOutput(
            id = data.id,
            title = data.data.title,
        )
    }

    override suspend fun createServiceCategory(input: CreateServiceCategoryInput): CreateServiceCategoryOutput {
        val response =
            client.mutation(AddServiceCategoryMutation(input.companyId, input.title)).execute()
        val data = response.dataAssertNoErrors.company.addServiceCategory
        return CreateServiceCategoryOutput(
            id = data.id,
            title = data.data.title
        )
    }
}