package ru.mclient.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named

class KtorServiceNetworkSource(
    @Named("authorized")
    val client: HttpClient,
) : ServiceNetworkSource {

    override suspend fun getServicesForCategoryAndCompany(input: GetServicesForCategoryAndCompanyInput): GetServicesForCategoryAndCompanyOutput {
        val response = client.get("/categories/${input.categoryId}/services") {
            url { parameter("companyId", input.companyId) }
        }
        val body = response.body<GetServicesForCategoryResponse>()
        return GetServicesForCategoryAndCompanyOutput(
            services = body.services.map {
                GetServicesForCategoryAndCompanyOutput.Service(
                    id = it.id.toString(),
                    categoryId = it.categoryId.toString(),
                    title = it.title,
                    cost = it.cost,
                    formattedCost = it.cost.toString(),
                )
            }
        )
    }

    override suspend fun createService(input: CreateServiceInput): CreateServiceOutput {
        val response = client.post("/categories/${input.categoryId}/services") {
            setBody(
                CreateServiceRequest(
                    title = input.title,
                    cost = input.cost,
                    companyId = input.companyId.toLong(),
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateServiceResponse>()
        return CreateServiceOutput(
            id = body.id.toString(),
            title = body.title,
            cost = body.cost.toString(),
            description = "",
            categoryId = body.categoryId.toString(),
        )
    }

    override suspend fun getServiceById(input: GetServiceByIdInput): GetServiceByIdOutput {
        val response = client.get("/services/${input.serviceId}")
        val body = response.body<GetServiceResponse>()
        return GetServiceByIdOutput(
            id = body.service.id.toString(),
            title = body.service.title,
            description = "",
            cost = body.service.cost.toString(),
        )
    }

    override suspend fun getServiceAnalytics(input: GetServiceAnalyticsInput): GetServiceAnalyticsOutput {
        TODO("Not yet implemented")
    }
}


@Serializable
class GetServicesForCategoryResponse(
    val services: List<Service>,
) {

    @Serializable
    class Service(
        val id: Long,
        val categoryId: Long,
        val title: String,
        val cost: Long,
    )

}

@Serializable
class CreateServiceRequest(
    val title: String,
    val cost: Long,
    val companyId: Long,
)


@Serializable
class CreateServiceResponse(
    val id: Long,
    val title: String,
    val cost: Long,
    val categoryId: Long,
)

@Serializable
class GetServiceResponse(
    val service: Service,
    val category: ServiceCategory,
) {
    @Serializable
    data class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}