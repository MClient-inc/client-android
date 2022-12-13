package ru.mclient.network.staff

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
import java.time.LocalDate
import java.time.LocalTime

class KtorStaffNetworkSource(
    @Named("authorized")
    val client: HttpClient,
) : StaffNetworkSource {

    override suspend fun getStaffForCompanyAndSchedule(input: GetStaffForCompanyAndScheduleInput): GetStaffForCompanyAndScheduleOutput {
        val response = client.get("/companies/${input.companyId}/schedule") {
            parameter("date", input.date.toLocalDate().toString())
        }
        val body = response.body<GetMultipleStaffScheduleResponse>()
        return GetStaffForCompanyAndScheduleOutput(
            staff = body.schedule.map {
                GetStaffForCompanyAndScheduleOutput.Staff(
                    id = it.staff.id.toString(),
                    name = it.staff.name,
                    codename = it.staff.codename,
                    role = it.staff.role
                )
            }
        )
    }

    override suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput {
        val response = client.get("/companies/${input.companyId}/staff")
        val body = response.body<GetStaffForCompanyResponse>()
        return GetStaffForCompanyOutput(
            body.staff.map {
                GetStaffForCompanyOutput.Staff(
                    id = it.id.toString(),
                    name = it.name,
                    codename = it.codename,
                    role = it.role,
                )
            }
        )
    }

    override suspend fun getStaffById(input: GetStaffByIdInput): GetStaffByIdOutput {
        val response = client.get("/staff/${input.staffId}")
        val body = response.body<GetStaffResponse>()
        return GetStaffByIdOutput(
            id = body.id.toString(),
            name = body.name,
            codename = body.codename,
            role = body.role,
        )
    }

    override suspend fun createStaff(input: CreateStaffInput): CreateStaffOutput {
        val response = client.post("/companies/${input.companyId}/staff") {
            setBody(
                CreateStaffRequest(
                    name = input.name,
                    codename = input.codename,
                    role = input.role,
                ),
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<CreateStaffResponse>()
        return CreateStaffOutput(
            id = body.id.toString(),
            name = body.name,
            codename = body.codename,
            role = body.role,
        )
    }

    override suspend fun getStaffSchedule(input: GetStaffScheduleByIdInput): GetStaffScheduleByIdOutput {
        val response = client.get("/staff/${input.staffId}/schedule") {
            parameter("start", LocalDate.of(2000, 1, 1))
            parameter("end", LocalDate.of(2030, 12, 31))
        }
        val body = response.body<GetSingleStaffScheduleResponse>()
        return GetStaffScheduleByIdOutput(
            schedule = body.schedule.map {
                val time = it.slots.firstOrNull() ?: GetSingleStaffScheduleResponse.ScheduleSlot(
                    LocalTime.MIN,
                    LocalTime.MAX,
                )
                GetStaffScheduleByIdOutput.Schedule(
                    date = it.date,
                    start = time.to,
                    end = time.from,
                )
            }
        )
    }


    override suspend fun createStaffSchedule(input: CreateStaffScheduleInput): CreateStaffScheduleOutput {
        val response = client.post("/staff/${input.staffId}/schedule") {
            setBody(
                AddSingeStaffScheduleRequest(
                    schedule = input.schedule.flatMap {
                        val list = mutableListOf<AddSingeStaffScheduleRequest.ScheduleDate>()
                        for (i in (it.start..it.end)) {
                            list.add(
                                AddSingeStaffScheduleRequest.ScheduleDate(
                                    AddSingeStaffScheduleRequest.ScheduleOperation.ADD,
                                    date = i,
                                    slots = listOf(
                                        AddSingeStaffScheduleRequest.ScheduleSlot(
                                            operation = AddSingeStaffScheduleRequest.ScheduleOperation.ADD,
                                            from = LocalTime.of(10, 0),
                                            to = LocalTime.of(22, 0)
                                        )
                                    )
                                )
                            )
                        }
                        list
                    }
                ),
            )
            contentType(ContentType.Application.Json)
        }
        val body = response.body<GetSingleStaffScheduleResponse>()
        return CreateStaffScheduleOutput(
            staffId = input.staffId,
            schedule = body.schedule.map { it.date }
        )
    }
}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> {
    val longIt = (start.toEpochDay()..endInclusive.toEpochDay()).iterator()
    return object : Iterator<LocalDate> {
        override fun hasNext() = longIt.hasNext()
        override fun next(): LocalDate = LocalDate.ofEpochDay(longIt.nextLong())
    }
}

@Serializable
class CreateStaffRequest(
    val name: String,
    val codename: String,
    val role: String,
)

@Serializable
class CreateStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)

@Serializable
class GetStaffForCompanyResponse(
    val staff: List<Staff>,
) {
    @Serializable
    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )
}


@Serializable
class GetStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)

@Serializable
class GetMultipleStaffScheduleResponse(
    val schedule: List<ScheduleDate>,
) {
    @Serializable
    class ScheduleDate(
        val staff: Staff,
        @Contextual
        val date: LocalDate,
        val slots: List<ScheduleSlot>,
    )

    @Serializable
    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )

    @Serializable
    class ScheduleSlot(
        @Contextual
        val from: LocalTime,
        @Contextual
        val to: LocalTime,
    )
}

@Serializable
class GetSingleStaffScheduleResponse(
    val schedule: List<ScheduleDate>,
) {
    @Serializable
    class ScheduleDate(
        @Contextual
        val date: LocalDate,
        val slots: List<ScheduleSlot>,
    )

    @Serializable
    class ScheduleSlot(
        @Contextual
        val from: LocalTime,
        @Contextual
        val to: LocalTime,
    )
}

@Serializable
class AddSingeStaffScheduleRequest(
    val schedule: List<ScheduleDate>,
) {
    @Serializable
    class ScheduleDate(
        val operation: ScheduleOperation,
        @Contextual
        val date: LocalDate,
        val slots: List<ScheduleSlot> = emptyList(),
    )

    @Serializable
    class ScheduleSlot(
        val operation: ScheduleOperation,
        @Contextual
        val from: LocalTime,
        @Contextual
        val to: LocalTime,
    )

    enum class ScheduleOperation {
        ADD, DELETE
    }
}

