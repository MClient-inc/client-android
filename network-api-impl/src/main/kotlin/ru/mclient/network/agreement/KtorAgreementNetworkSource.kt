package ru.mclient.network.agreement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single


@Single
class KtorAgreementNetworkSource(
    @Named("unauthorized")
    private val client: HttpClient,
) : AgreementNetworkSource {

    override suspend fun getUserAgreement(): GetAgreementOutput {
        val response = client.get("/info/user-agreement")
        val body = response.body<GetUserAgreementResponse>()
        return GetAgreementOutput(
            title = body.title,
            content = body.content,
        )
    }

    override suspend fun getClientDataProcessingAgreement(): GetAgreementOutput {
        val response = client.get("/info/data-processing-agreement")
        val body = response.body<GetUserAgreementResponse>()
        return GetAgreementOutput(
            title = body.title,
            content = body.content,
        )
    }

}

@Serializable
data class GetUserAgreementResponse(
    val title: String,
    val content: String,
)