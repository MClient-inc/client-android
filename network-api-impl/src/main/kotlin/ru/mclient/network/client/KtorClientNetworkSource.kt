package ru.mclient.network.client

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
import java.time.LocalDate
import java.time.LocalTime

class KtorClientNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : ClientNetworkSource {

    override suspend fun createClient(input: CreateClientInput): CreateClientOutput {
        val response = client.post("/companies/${input.companyId}/clients") {
            setBody(
                CreateClientRequest(
                    name = input.name,
                    phone = if (input.phone.length == 11) input.phone else null
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateClientResponse>()
        return CreateClientOutput(
            id = body.id.toString(),
            name = body.name,
            phone = body.phone.orEmpty()
        )
    }

    override suspend fun findClientsForCompany(input: GetClientsForCompanyInput): GetClientsForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/clients")
        val body = response.body<GetClientsByCompany>()
        return GetClientsForCompanyOutput(
            clients = body.clients.map {
                GetClientsForCompanyOutput.Client(
                    id = it.id.toString(),
                    title = it.name,
                    phone = it.phone.orEmpty(),
                )
            }
        )
    }

    override suspend fun getClientCard(input: GetClientCardInput): GetClientCardOutput {
        val response = client.get("/clients/${input.clientId}/card")
        val body = response.body<GetCardResponse>()
        return GetClientCardOutput(body.cardUrl)
    }

    override suspend fun getClientAnalytics(input: GetClientAnalyticsInput): GetClientAnalyticsOutput {
        TODO("Not yet implemented")
    }

    override suspend fun getClientById(input: GetClientByIdInput): GetClientByIdOutput {
        val response = client.get("/clients/${input.clientId}")
        val body = response.body<GetClientById>()
        return GetClientByIdOutput(
            id = body.id.toString(),
            name = body.name,
            phone = body.phone.orEmpty()
        )
    }
//
//    override suspend fun getClientAnalytics(input: GetClientAnalyticsInput): GetClientAnalyticsOutput {
//        val response = client.get("")
//        val body = response.body<GetClientAnalyticResponse>()
//        return GetClientAnalyticsOutput(
//            upcomingRecords = body.upcomingRecords.map { record ->
//                GetClientAnalyticsOutput.Record(
//                    id = record.id,
//                    totalCost = record.totalCost,
//                    time = GetClientAnalyticsOutput.Time(
//                        start = record.time.start,
//                        end = record.time.end,
//                        date = record.time.date
//                    ),
//                    company = GetClientAnalyticsOutput.Company(
//                        id = record.company.id,
//                        title = record.company.title
//                    ),
//                    staff = GetClientAnalyticsOutput.Staff(
//                        id = record.staff.id,
//                        name = record.staff.name
//                    ),
//                    services = record.services.map { service ->
//                        GetClientAnalyticsOutput.Service(
//                            id = service.id,
//                            cost = service.cost,
//                            title = service.title
//                        )
//                    }
//                )
//            },
//            company = GetClientAnalyticsOutput.CompanyAnalytics(
//                id = body.company?.id,
//                title = body.company?.title,
//                analytics = GetClientAnalyticsOutput.ClientAnalyticsItem(
//                    notComeCount = body.company.analytics.notComeCount,
//
//                )
//            ) ?: null
//        )
//    }

}

@Serializable
class GetCardResponse(
    val cardUrl: String,
)


@Serializable
class CreateClientRequest(
    val name: String,
    val phone: String?,
)

@Serializable
class CreateClientResponse(
    val id: Long,
    val name: String,
    val phone: String?,
    val networkId: Long,
)

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

@Serializable
class GetClientById(
    val id: Long,
    val name: String,
    val phone: String?,
)

@Serializable
class GetClientAnalyticResponse(
    val network: NetworkAnalytics,
    val company: CompanyAnalytics?,
    val upcomingRecords: List<Record>,
) {
    @Serializable
    class NetworkAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    @Serializable
    class CompanyAnalytics(
        val id: Long,
        val title: String,
        val analytics: ClientAnalyticsItem,
    )

    @Serializable
    class ClientAnalyticsItem(
        var notComeCount: Long,
        var comeCount: Long,
        var waitingCount: Long,
        val totalCount: Long,
    )

    @Serializable
    class Record(
        val id: Long,
        val company: Company,
        val time: Time,
        val staff: Staff,
        val services: List<Service>,
        val totalCost: Long,
    )

    @Serializable
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    class Staff(
        val id: Long,
        val name: String,
    )

    @Serializable
    class Time(
        @Contextual
        val date: LocalDate,
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
    )

    @Serializable
    class Company(
        val id: Long,
        val title: String,
    )

}