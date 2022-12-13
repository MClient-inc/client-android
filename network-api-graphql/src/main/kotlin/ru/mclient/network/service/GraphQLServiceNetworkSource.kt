package ru.mclient.network.service

import com.apollographql.apollo3.ApolloClient
import org.koin.core.annotation.Single
import ru.mclient.network.data.AddServiceMutation
import ru.mclient.network.data.GetServiceAnalyticsQuery
import ru.mclient.network.data.GetServiceQuery
import ru.mclient.network.data.GetServicesForCategoryQuery
import ru.mclient.network.data.fragment.ServiceAnalyticsFragment
import ru.mclient.network.data.type.DateRangeInput

@Single
class GraphQLServiceNetworkSource(
    private val client: ApolloClient,
) : ServiceNetworkSource {

    override suspend fun getServicesForCategoryAndCompany(input: GetServicesForCategoryAndCompanyInput): GetServicesForCategoryAndCompanyOutput {
        val response = client.query(
            GetServicesForCategoryQuery(
                input.categoryId,
                input.companyId,
            )
        ).execute()
        return GetServicesForCategoryAndCompanyOutput(response.dataAssertNoErrors.serviceCategory.services.map {
            GetServicesForCategoryAndCompanyOutput.Service(
                it.basicService.id,
                it.basicService.serviceCategory.id,
                it.basicService.data.title,
                it.basicService.data.cost.basicCost.rawCost,
                it.basicService.data.cost.basicCost.formattedCost,
            )
        }
        )
    }

    override suspend fun createService(input: CreateServiceInput): CreateServiceOutput {
        val response = client.mutation(
            AddServiceMutation(
                categoryId = input.categoryId,
                title = input.title,
                cost = input.cost,
                durationInMinutes = 60,
                companies = listOf(input.companyId)
            )
        ).execute()
        val data = response.dataAssertNoErrors.serviceCategory.addService.basicService
        return CreateServiceOutput(
            id = data.id,
            title = data.data.title,
            cost = data.data.cost.basicCost.formattedCost,
            description = data.data.description.orEmpty(),
            categoryId = data.serviceCategory.id,
        )
    }

    override suspend fun getServiceById(input: GetServiceByIdInput): GetServiceByIdOutput {
        val response = client.query(GetServiceQuery(input.serviceId)).execute()
        val body = response.dataAssertNoErrors.service.basicService
        return GetServiceByIdOutput(
            id = body.id,
            title = body.data.title,
            description = body.data.description.orEmpty(),
            cost = body.data.cost.basicCost.formattedCost,
        )
    }


    override suspend fun getServiceAnalytics(input: GetServiceAnalyticsInput): GetServiceAnalyticsOutput {
        val response = client.query(
            GetServiceAnalyticsQuery(
                input.id,
                input.companyId,
                DateRangeInput(),
            )
        ).execute()
        val data = response.dataAssertNoErrors
        val network = data.service.analytics.network.serviceAnalyticsFragment
        val company = data.service.analytics.company.serviceAnalyticsFragment
        return GetServiceAnalyticsOutput(
            network = GetServiceAnalyticsOutput.NetworkAnalytics(
                data.service.network.id,
                data.service.network.data.title,
                network.toData(),
            ),
            company = GetServiceAnalyticsOutput.CompanyAnalytics(
                data.company.id,
                data.company.data.title,
                company.toData()
            )
        )
    }

    private fun ServiceAnalyticsFragment.toData(): GetServiceAnalyticsOutput.AnalyticsItem {
        return GetServiceAnalyticsOutput.AnalyticsItem(
            comeCount = comeCount,
            notComeCount = notComeCount,
            waitingCount = waitingCount,
            totalRecords = totalCount,
            popularity = popularity
        )
    }

}