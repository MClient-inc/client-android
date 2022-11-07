package ru.mclient.network.abonement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDateTime

@Single
class KtorAbonementNetworkSource(
    @Named("authorized")
    val client: HttpClient,
) : AbonementNetworkSource {

    override suspend fun getAbonementById(input: GetAbonementByIdInput): GetAbonementByIdOutput {
        val response = client.get("/abonements/${input.abonementId}")
        val body = response.body<GetAbonementByIdResponse>()
        return GetAbonementByIdOutput(
            abonement = GetAbonementByIdOutput.Abonement(
                id = body.id,
                subabonements = body.subabonements.map {
                    GetAbonementByIdOutput.Subabonement(
                        id = it.id,
                        liveTimeInMillis = it.liveTimeInMillis,
                        availableUntil = it.availableUntil,
                        usages = it.usages,
                        title = it.title
                    )
                },
                title = body.title,
                services = body.services.map {
                    GetAbonementByIdOutput.Service(
                        id = it.id,
                        title = it.title,
                        cost = it.cost,
                    )
                }
            )
        )
    }

    override suspend fun getAbonementsForCompany(input: GetAbonementsForCompanyInput): GetAbonementsForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/abonements")
        val body = response.body<GetAbonementsResponse>()
        return GetAbonementsForCompanyOutput(
            abonements = body.abonements.map { abonement ->
                GetAbonementsForCompanyOutput.Abonement(
                    abonement.id,
                    abonement.title,
                    abonement.subabonements.map {
                        GetAbonementsForCompanyOutput.Subabonement(
                            id = it.id,
                            title = it.title,
                            usages = it.usages,
                            liveTimeInMillis = it.liveTimeInMillis,
                            availableUntil = it.availableUntil
                        )
                    }
                )
            }
        )
    }

    override suspend fun createAbonement(input: CreateAbonementInput): CreateAbonementOutput {
        val response = client.post("/companies/${input.companyId}/abonements") {
            setBody(
                CreateAbonementRequest(
                    input.title,
                    input.subabonements.map {
                        CreateAbonementRequest.Subabonement(
                            it.title,
                            it.usages,
                        )
                    },
                    input.services
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateAbonementResponse>()
        return CreateAbonementOutput(
            abonement = CreateAbonementOutput.Abonement(
                id = body.id,
                title = body.title,
                subabonements = body.subabonements.map {
                    CreateAbonementOutput.Subabonement(
                        id = it.id,
                        title = it.title,
                        usages = it.usages,
                        liveTimeInMillis = it.liveTimeInMillis,
                        availableUntil = it.availableUntil,
                    )
                },
                services = body.services.map {
                    CreateAbonementOutput.Service(
                        id = it.id,
                        title = it.title,
                        cost = it.cost,
                    )
                }
            )
        )
    }
}

@Serializable
data class CreateAbonementRequest(
    val title: String,
    val subabonements: List<Subabonement>,
    val services: List<Long>,
) {

    @Serializable
    class Subabonement(
        val title: String,
        val usages: Int,
    )

}

@Serializable
data class CreateAbonementResponse(
    val id: Long,
    val title: String,
    val subabonements: List<Subabonement>,
    val services: List<PairedService>,
) {
    @Serializable
    class PairedService(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        @Contextual
        val availableUntil: LocalDateTime,
    )

}


@Serializable
class GetAbonementByIdResponse(
    val id: Long,
    val services: List<Service>,
    val subabonements: List<Subabonement>,
    val title: String,
) {

    @Serializable
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        @Contextual
        val availableUntil: LocalDateTime,
    )

}


@Serializable
class GetAbonementsResponse(
    val abonements: List<Abonement>,
) {

    @Serializable
    class Abonement(
        val id: Long,
        val title: String,
        val subabonements: List<Subabonement>,
    )

    @Serializable
    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        @Contextual
        val availableUntil: LocalDateTime,
    )

}