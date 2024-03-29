package ru.mclient.network.record

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
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
        val payResponse = client.get("/records/${input.recordId}/pay")
        val body = response.body<GetRecordByIdResponse>()
        val payBody = payResponse.body<GetRecordPaymentResponse>()
        return GetRecordByIdOutput(
            record = GetRecordByIdOutput.Record(
                id = body.id,
                services = body.services.map {
                    GetRecordByIdOutput.Service(
                        id = it.id,
                        cost = it.cost,
                        title = it.title,
                    )
                },
                staff = GetRecordByIdOutput.Staff(
                    id = body.staff.id,
                    codename = body.staff.codename,
                    name = body.staff.name,
                    role = body.staff.role
                ),
                schedule = GetRecordByIdOutput.Schedule(
                    id = body.schedule.id,
                    date = body.schedule.date,
                    start = body.schedule.start,
                    end = body.schedule.end
                ),
                time = GetRecordByIdOutput.TimeOffset(
                    start = body.time.start,
                    end = body.time.end
                ),
                totalCost = body.totalCost,
                client = GetRecordByIdOutput.Client(
                    id = body.client.id,
                    phone = body.client.phone,
                    name = body.client.name
                ),
                status = when (body.status) {
                    GetRecordByIdResponse.RecordVisitStatus.WAITING -> RecordVisitStatus.WAITING
                    GetRecordByIdResponse.RecordVisitStatus.COME -> RecordVisitStatus.COME
                    GetRecordByIdResponse.RecordVisitStatus.NOT_COME -> RecordVisitStatus.NOT_COME
                },
                abonements = payBody.abonements.map {
                    GetRecordByIdOutput.ClientAbonement(
                        id = it.id,
                        usages = it.usages,
                        abonement = GetRecordByIdOutput.Abonement(
                            id = it.abonement.id,
                            title = it.abonement.title,
                            subabonement = GetRecordByIdOutput.Subabonement(
                                id = it.abonement.subabonement.id,
                                title = it.abonement.subabonement.title,
                                maxUsages = it.abonement.subabonement.maxUsages,
                                cost = it.abonement.subabonement.cost,
                            )
                        )
                    )
                }
            )
        )

    }

    override suspend fun editRecordStatus(input: EditRecordStatusInput): EditRecordStatusOutput {
        client.patch("/records/${input.recordId}/status") {
            setBody(
                EditRecordStatusRequest(
                    when (input.status) {
                        RecordVisitStatus.WAITING -> EditRecordStatusRequest.RecordVisitStatus.WAITING
                        RecordVisitStatus.COME -> EditRecordStatusRequest.RecordVisitStatus.COME
                        RecordVisitStatus.NOT_COME -> EditRecordStatusRequest.RecordVisitStatus.NOT_COME
                    }
                )
            )
            contentType(ContentType.Application.Json)
        }
        return EditRecordStatusOutput(
            input.recordId,
            when (input.status) {
                RecordVisitStatus.WAITING -> EditRecordStatusOutput.RecordVisitStatus.WAITING
                RecordVisitStatus.COME -> EditRecordStatusOutput.RecordVisitStatus.COME
                RecordVisitStatus.NOT_COME -> EditRecordStatusOutput.RecordVisitStatus.NOT_COME
            },
        )
    }

    override suspend fun payWithAbonements(input: PayWithAbonementsInput): PayWithAbonementsOutput {
        client.patch("/records/${input.recordId}/pay") {
            setBody(
                EditRecordVisitRequest(
                    abonements = input.abonements,
                )
            )
            contentType(ContentType.Application.Json)
        }
        return PayWithAbonementsOutput(
            input.recordId,
        )
    }
}


@Serializable
class EditRecordVisitRequest(
    val abonements: List<Long>,
)

@Serializable
class EditRecordStatusRequest(
    val status: RecordVisitStatus,
) {
    enum class RecordVisitStatus {
        WAITING, COME, NOT_COME
    }
}


@Serializable
class GetRecordByIdResponse(
    val id: Long,
    val client: Client,
    val schedule: Schedule,
    val time: TimeOffset,
    val services: List<Service>,
    val staff: Staff,
    val totalCost: Long,
    val status: RecordVisitStatus,
) {

    enum class RecordVisitStatus {
        WAITING, COME, NOT_COME
    }

    @Serializable
    class Staff(
        val id: Long,
        val name: String,
        val role: String,
        val codename: String,
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
        val id: Long,
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


@Serializable
class GetRecordPaymentResponse(
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
        val maxUsages: Int,
        val cost: Long,
    )

}