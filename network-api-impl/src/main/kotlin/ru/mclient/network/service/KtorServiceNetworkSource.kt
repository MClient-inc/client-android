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
import org.koin.core.annotation.Single

@Single
class KtorServiceNetworkSource(
    @Named("authorized")
    val client: HttpClient
) : ServiceNetworkSource {

    override suspend fun getServicesForCategoryAndCompany(input: GetServicesForCategoryAndCompanyInput): GetServicesForCategoryAndCompanyOutput {
        val response = client.get("/categories/${input.categoryId}/services") {
            url { parameter("companyId", input.companyId) }
        }
        val body = response.body<GetServicesForCategoryResponse>()
        return GetServicesForCategoryAndCompanyOutput(
            services = body.services.map {
                GetServicesForCategoryAndCompanyOutput.Service(
                    id = it.id,
                    categoryId = it.categoryId,
                    title = it.title,
                    cost = it.cost,
                )
            }
        )
    }

    override suspend fun createService(input: CreateServiceInput): CreateServiceOutput {
        val response = client.post("/categories/${input.categoryId}/services") {
            setBody(
                CreateServiceRequest(
                    title = input.title,
                    cost = input.cost.toLongOrNull() ?: 0,
                    companyId = input.companyId,
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateServiceResponse>()
        return CreateServiceOutput(
            id = body.id,
            title = body.title,
            cost = body.cost.toString(),
            description = "",
            categoryId = body.categoryId,
        )
    }

    override suspend fun getServiceById(input: GetServiceByIdInput): GetServiceByIdOutput {
        val response = client.get("/services/${input.serviceId}")
        val body = response.body<GetServiceResponse>()
        return GetServiceByIdOutput(
            id = body.service.id,
            title = body.service.title,
            description = "",
            cost = body.service.cost.toString(),
        )
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