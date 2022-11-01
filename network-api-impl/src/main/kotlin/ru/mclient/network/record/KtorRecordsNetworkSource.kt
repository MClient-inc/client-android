package ru.mclient.network.record

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.time.LocalTime

@Single
class KtorRecordsNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : RecordsNetworkSource {

    override suspend fun getRecordsForCompany(input: GetRecordsForCompanyInput): GetRecordsForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/records") {
            parameter("limit", 5)
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
                            from = group.schedule.start,
                            to = group.schedule.end,
                            staff = GetRecordsForCompanyOutput.Staff(
                                id = group.schedule.staff.id,
                                name = group.schedule.staff.name,
                            ),
                        ),
                        services = record.services.map { service ->
                            GetRecordsForCompanyOutput.Service(
                                id = service.id,
                                title = service.title
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