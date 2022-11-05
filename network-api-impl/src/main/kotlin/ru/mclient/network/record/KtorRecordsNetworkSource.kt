package ru.mclient.network.record

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Single
class KtorRecordsNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : RecordsNetworkSource {

    override suspend fun getRecordsForCompany(input: GetRecordsForCompanyInput): GetRecordsForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/records") {
            parameter("limit", input.limit)
        }
        val body = response.body<GetRecordsResponse>()
        return GetRecordsForCompanyOutput(
            records = body.records.flatMap { group ->
                group.records.map { record ->
                    GetRecordsForCompanyOutput.Record(
                        id = record.id,
                        client = GetRecordsForCompanyOutput.Client(
                            id = record.client.id,
                            name = record.client.name,
                            record.client.phone
                        ),
                        schedule = GetRecordsForCompanyOutput.Schedule(
                            date = group.schedule.date,
                            start = group.schedule.start,
                            end = group.schedule.end,
                            staff = GetRecordsForCompanyOutput.Staff(
                                id = group.schedule.staff.id,
                                name = group.schedule.staff.name,
                            ),
                        ),
                        services = record.services.map { service ->
                            GetRecordsForCompanyOutput.Service(
                                id = service.id,
                                title = service.title,
                                cost = service.cost,
                            )
                        },
                        time = GetRecordsForCompanyOutput.TimeOffset(
                            start = record.time.start,
                            end = record.time.end,
                        )
                    )
                }
            }
        )
    }

    override suspend fun createRecord(input: CreateRecordInput): CreateRecordOutput {
        val response = client.post("/companies/${input.companyId}/records") {
            setBody(
                CreateRecordRequest(
                    input.staffId,
                    input.clientId,
                    input.dateTime,
                    emptyList()
                )
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateRecordResponse>()
        return CreateRecordOutput(
            record = CreateRecordOutput.Record(
                id = body.id,
                client = CreateRecordOutput.Client(
                    id = body.client.id,
                    name = body.client.name,
                    phone = body.client.phone
                ),
                schedule = CreateRecordOutput.Schedule(
                    staff = CreateRecordOutput.Staff(
                        id = body.schedule.staff.id,
                        name = body.schedule.staff.name,
                    ),
                    start = body.schedule.start,
                    end = body.schedule.end,
                    date = body.schedule.date,
                ),
                services = body.services.map {
                    CreateRecordOutput.Service(
                        id = it.id,
                        title = it.title,
                        cost = it.cost,
                    )
                },
                time = CreateRecordOutput.TimeOffset(
                    start = body.time.start,
                    end = body.time.end,
                )
            )
        )
    }

}

@Serializable
class GetRecordsResponse(
    val records: List<RecordsGroup>,
) {

    @Serializable
    class RecordsGroup(
        val schedule: Schedule,
        val records: List<Record>,
    )

    @Serializable
    class Record(
        val id: Long,
        val time: TimeOffset,
        val client: Client,
        val services: List<Service>,
    )

    @Serializable
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )

    @Serializable
    class Staff(
        val name: String,
        val id: Long,
    )

    @Serializable
    class TimeOffset(
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
    )

    @Serializable
    class Schedule(
        val id: Long,
        @Contextual
        val date: LocalDate,
        val staff: Staff,
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
    )

}

@Serializable
class CreateRecordRequest(
    val staffId: Long,
    val clientId: Long,
    @Contextual
    val dateTime: LocalDateTime,
    val services: List<Long>,
)

@Serializable
class CreateRecordResponse(
    val id: Long,
    val time: TimeOffset,
    val client: Client,
    val services: List<Service>,
    val schedule: Schedule,
) {

    @Serializable
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    @Serializable
    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )

    @Serializable
    class Staff(
        val name: String,
        val id: Long,
    )

    @Serializable
    class TimeOffset(
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
    )

    @Serializable
    class Schedule(
        val id: Long,
        @Contextual
        val date: LocalDate,
        val staff: Staff,
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
    )
}