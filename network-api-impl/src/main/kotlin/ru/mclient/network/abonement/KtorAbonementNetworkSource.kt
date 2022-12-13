package ru.mclient.network.abonement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import java.time.LocalDateTime

class KtorAbonementNetworkSource(
    @Named("authorized")
    val client: HttpClient,
) : AbonementNetworkSource {

    override suspend fun getAbonementById(input: GetAbonementByIdInput): GetAbonementByIdOutput {
        val response = client.get("/abonements/${input.abonementId}")
        val body = response.body<GetAbonementByIdResponse>()
        return GetAbonementByIdOutput(
            abonement = GetAbonementByIdOutput.Abonement(
                id = body.id.toString(),
                subabonements = body.subabonements.map {
                    GetAbonementByIdOutput.Subabonement(
                        id = it.id.toString(),
                        liveTimeInMillis = it.liveTimeInMillis,
                        availableUntil = it.availableUntil,
                        usages = it.usages,
                        title = it.title
                    )
                },
                title = body.title,
                services = body.services.map {
                    GetAbonementByIdOutput.Service(
                        id = it.id.toString(),
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
                    abonement.id.toString(),
                    abonement.title,
                    abonement.subabonements.map {
                        GetAbonementsForCompanyOutput.Subabonement(
                            id = it.id.toString(),
                            title = it.title,
                            cost = it.cost,
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
                            title = it.title,
                            usages = it.usages,
                            cost = it.cost,
                        )
                    },
                    input.services.map(String::toLong)
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateAbonementResponse>()
        return CreateAbonementOutput(
            abonement = CreateAbonementOutput.Abonement(
                id = body.id.toString(),
                title = body.title,
                subabonements = body.subabonements.map {
                    CreateAbonementOutput.Subabonement(
                        id = it.id.toString(),
                        title = it.title,
                        usages = it.usages,
                        cost = it.cost,
                        liveTimeInMillis = it.liveTimeInMillis,
                        availableUntil = it.availableUntil,
                    )
                },
                services = body.services.map {
                    CreateAbonementOutput.Service(
                        id = it.id.toString(),
                        title = it.title,
                        cost = it.cost,
                    )
                }
            )
        )
    }

    override suspend fun getAbonementsForClient(input: GetAbonementsForClientInput): GetAbonementsForClientOutput {
        val response = client.get("/clients/${input.clientId}/abonements")
        val body = response.body<GetClientAbonementsResponse>()
        return GetAbonementsForClientOutput(
            abonements = body.abonements.map {
                GetAbonementsForClientOutput.ClientAbonement(
                    id = it.id.toString(),
                    usages = it.usages,
                    abonement = GetAbonementsForClientOutput.Abonement(
                        id = it.abonement.id.toString(),
                        title = it.abonement.title,
                        subabonement = GetAbonementsForClientOutput.Subabonement(
                            id = it.abonement.subabonement.id.toString(),
                            title = it.abonement.subabonement.title,
                            cost = it.abonement.subabonement.cost,
                            maxUsages = it.abonement.subabonement.maxUsages,
                        )
                    )
                )
            }
        )
    }

    override suspend fun addAbonementForClient(input: AddAbonementToClientInput): AddAbonementToClientOutput {
        client.put("/clients/${input.clientId}/abonements") {
            setBody(
                AddAbonementToClientRequest(
                    subabonementId = input.subabonementId.toLong(),
                )
            )
            contentType(ContentType.Application.Json)
        }
        return AddAbonementToClientOutput(input.clientId)
    }
}

@Serializable
class AddAbonementToClientRequest(
    val subabonementId: Long,
)


@Serializable
class GetClientAbonementsResponse(
    val abonements: List<ClientAbonement>,
) {

    @Serializable
    class ClientAbonement(
        val id: Long,
        val abonement: Abonement,
        val usages: Int,
        val cost: Long,
    )

    @Serializable
    class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )


    @Serializable
    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val cost: Long,
        val maxUsages: Int,
    )

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
        val cost: Long,
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
        val cost: Long,
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
        val cost: Long,
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
        val cost: Long,
        val liveTimeInMillis: Long,
        @Contextual
        val availableUntil: LocalDateTime,
    )

}