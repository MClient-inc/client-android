package ru.mclient.network.servicecategory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
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
                    id = category.id,
                    title = category.title
                )
            }
        )
    }

    override suspend fun getServiceCategoryById(input: GetServiceCategoryByIdInput): GetServiceCategoryByIdOutput {
        val response = client.get("/categories/${input.categoryId}")
        val body = response.body<GetServiceCategoryResponse>()
        return GetServiceCategoryByIdOutput(
            id = body.id,
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
