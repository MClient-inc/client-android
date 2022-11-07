package ru.mclient.network.abonnement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDateTime

@Single
class KtorAbonnementNetworkSource(
    @Named("authorized")
    val client: HttpClient
): AbonnementNetworkSource {
    override suspend fun getAbonnementById(input: GetAbonnementByIdInput): GetAbonnementByIdOutput {
        val response = client.get("/abonements/${input.abonnementId}")
        val body = response.body<GetAbonnementByIdResponse>()
        return GetAbonnementByIdOutput(
            abonnement = GetAbonnementByIdOutput.Abonnement(
                id = body.abonnement.id,
                subabonements = body.abonnement.subabonements.map {
                    GetAbonnementByIdOutput.SubAbonnement(
                        id = it.id,
                        liveTimeInMillis = it.liveTimeInMillis,
                        availableUntil = it.availableUntil,
                        usages = it.usages,
                        title = it.title
                    )
                },
                title = body.abonnement.title
            )
        )
    }


}

@Serializable
class GetAbonnementByIdResponse(
    val abonnement: Abonnement
) {
    @Serializable
    class Abonnement(
        val id: Long,
//        val services: List<Service>,
        val subabonements: List<SubAbonnement>,
        val title: String
    )

//    @Serializable
//    class Service(
//        val id: Long,
//        val title: String,
//        val cost: Long
//    )

    @Serializable
    class SubAbonnement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        @Contextual
        val availableUntil: LocalDateTime
    )
}