package ru.mclient.network.account

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Serializable
class GetAccountResponse(
    val username: String,
)

@Single
class KtorAccountNetworkSource(
    @Named("authorized")
    private val client: HttpClient
) : AccountNetworkSource {


    override suspend fun getBaseCurrentProfileInfo(): GetBaseCurrentProfileInfoOutput {
        val response = client.get("/account")
        val body = response.body<GetAccountResponse>()
        return GetBaseCurrentProfileInfoOutput(
            username = body.username,
            name = body.username,
            avatar = null
        )
    }

}