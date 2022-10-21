package ru.mclient.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
                )
            }
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
    )

}