package ru.mclient.network.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class KtorClientNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : ClientNetworkSource {

    override suspend fun findClientsForCompany(input: GetClientsForCompanyInput): GetClientsForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/clients")
        val body = response.body<GetClientsByCompany>()
        return GetClientsForCompanyOutput(
            clients = body.clients.map {
                GetClientsForCompanyOutput.Client(
                    id = it.id,
                    title = it.name,
                    phone = it.phone.orEmpty(),
                )
            }
        )
    }

}


@Serializable
class GetClientsByCompany(
    val clients: List<Client>,
) {
    @Serializable
    class Client(
        val id: Long,
        val name: String,
        val phone: String?,
    )
}