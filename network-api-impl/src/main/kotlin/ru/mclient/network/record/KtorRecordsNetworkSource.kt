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
                    input.services,
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

    override suspend fun getRecordById(input: GetRecordByIdInput): GetRecordByIdOutput {
        val response = client.get("/records/${input.recordId}")
        val body = response.body<GetRecordByIdResponse>()
        return GetRecordByIdOutput(
            record = GetRecordByIdOutput.Record(
                id = body.record.id,
                services = body.record.services.map {
                    GetRecordByIdOutput.Service(
                        id = it.id,
                        cost = it.cost,
                        title = it.title,
                        description = it.description
                    )
                },
                staff = GetRecordByIdOutput.Staff(
                    id = body.record.staff.id,
                    codename = body.record.staff.codename,
                    name = body.record.staff.name,
                    role = body.record.staff.role
                ),
                schedule = GetRecordByIdOutput.Schedule(
                    id = body.record.schedule.id,
                    date = body.record.schedule.date,
                    start = body.record.schedule.start,
                    end = body.record.schedule.end
                ),
                time = GetRecordByIdOutput.TimeOffset(
                    start = body.record.time.start,
                    end = body.record.time.end
                ),
                totalCost = body.record.totalCost,
                client = GetRecordByIdOutput.Client(
                    id = body.record.client.id,
                    phone = body.record.client.phone,
                    name = body.record.client.name
                )
            )
        )

    }
}


@Serializable
class GetRecordByIdResponse(
    val record: Record
) {
    @Serializable
    class Record(
        val id: Long,
        val client: Client,
        val schedule: Schedule,
        val time: TimeOffset,
        val services: List<Service>,
        val staff: Staff,
        val totalCost: Long
    )

    @Serializable
    class Staff(
        val id: Long,
        val name: String,
        val role: String,
        val codename: String
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
        @Contextual
        val date: LocalDate,
        @Contextual
        val start: LocalTime,
        @Contextual
        val end: LocalTime,
        val id: Long
    )

    @Serializable
    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )

    @Serializable
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
        val description: String
    )
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
