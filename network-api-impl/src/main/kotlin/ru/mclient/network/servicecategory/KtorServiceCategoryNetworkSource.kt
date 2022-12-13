package ru.mclient.network.servicecategory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named

class KtorServiceCategoryNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : ServiceCategoryNetworkSource {

    override suspend fun getServiceCategoriesByCompany(input: GetServiceCategoriesByCompanyInput): GetServiceCategoriesByCompanyOutput {
        val response = client.get("/companies/${input.companyId}/categories")
        val body = response.body<GetServiceCategoriesForCompanyResponse>()
        return GetServiceCategoriesByCompanyOutput(
            categories = body.categories.map { category ->
                GetServiceCategoriesByCompanyOutput.ServiceCategory(
                    id = category.id.toString(),
                    title = category.title
                )
            }
        )
    }

    override suspend fun getServiceCategoryById(input: GetServiceCategoryByIdInput): GetServiceCategoryByIdOutput {
        val response = client.get("/categories/${input.categoryId}")
        val body = response.body<GetServiceCategoryResponse>()
        return GetServiceCategoryByIdOutput(
            id = body.id.toString(),
            title = body.title,
        )
    }

    override suspend fun createServiceCategory(input: CreateServiceCategoryInput): CreateServiceCategoryOutput {
        val response = client.post("/companies/${input.companyId}/categories") {
            setBody(CreateServiceCategoryRequest(input.title))
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateServiceCategoryResponse>()
        return CreateServiceCategoryOutput(
            id = body.id.toString(),
            title = body.title,
        )
    }
}

@Serializable
class GetServiceCategoryResponse(
    val id: Long,
    val title: String,
)


@Serializable
class GetServiceCategoriesForCompanyResponse(
    val categories: List<ServiceCategory>,
) {
    @Serializable
    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}


@Serializable
class CreateServiceCategoryRequest(
    val title: String,
)

@Serializable
data class CreateServiceCategoryResponse(
    val id: Long,
    val title: String,
)