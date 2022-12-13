package ru.mclient.network.staff

import com.apollographql.apollo3.ApolloClient
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import org.koin.core.annotation.Single
import ru.mclient.network.data.AddStaffMutation
import ru.mclient.network.data.EditStaffScheduleMutation
import ru.mclient.network.data.GetCompanyStaffScheduleQuery
import ru.mclient.network.data.GetStaffForCompanyQuery
import ru.mclient.network.data.GetStaffQuery
import ru.mclient.network.data.GetStaffScheduleQuery
import ru.mclient.network.data.type.EditStaffScheduleInput
import ru.mclient.network.data.type.EditStaffScheduleSlotInput
import ru.mclient.network.data.type.OperationType
import java.time.LocalDate


@Single
class GraphQLStaffNetworkSource(
    private val client: ApolloClient,
) : StaffNetworkSource {

    override suspend fun getStaffForCompany(input: GetStaffForCompanyInput): GetStaffForCompanyOutput {
        val response = client.query(GetStaffForCompanyQuery(input.companyId)).execute()
        return GetStaffForCompanyOutput(response.dataAssertNoErrors.company.staff.map {
            val staff = it.basicStaff
            GetStaffForCompanyOutput.Staff(
                id = staff.id,
                codename = staff.codename,
                name = staff.data.name,
                role = staff.data.role,
            )
        })
    }

    override suspend fun getStaffForCompanyAndSchedule(input: GetStaffForCompanyAndScheduleInput): GetStaffForCompanyAndScheduleOutput {
        val response = client.query(
            GetCompanyStaffScheduleQuery(
                input.companyId,
                input.date.toKotlinLocalDateTime()
            )
        ).execute()
        return GetStaffForCompanyAndScheduleOutput(
            staff = response.dataAssertNoErrors.company.schedule.map {
                GetStaffForCompanyAndScheduleOutput.Staff(
                    id = it.staff.id,
                    name = it.staff.data.name,
                    codename = it.staff.codename,
                    role = it.staff.data.role,
                )
            }
        )
    }

    override suspend fun getStaffById(input: GetStaffByIdInput): GetStaffByIdOutput {
        val response = client.query(GetStaffQuery(input.staffId)).execute()
        val staff = response.dataAssertNoErrors.staff.basicStaff
        return GetStaffByIdOutput(
            id = staff.id,
            codename = staff.codename,
            name = staff.data.name,
            role = staff.data.role,
        )
    }

    override suspend fun getStaffSchedule(input: GetStaffScheduleByIdInput): GetStaffScheduleByIdOutput {
        val response = client.query(
            GetStaffScheduleQuery(
                company = input.companyId,
                staff = input.staffId
            )
        ).execute()
        return GetStaffScheduleByIdOutput(
            response.dataAssertNoErrors.staff.schedules.map {
                GetStaffScheduleByIdOutput.Schedule(
                    it.date.toJavaLocalDate(),
                    start = it.slots.first().start.toJavaLocalTime(),
                    end = it.slots.first().end.toJavaLocalTime()
                )
            }
        )
    }

    override suspend fun createStaff(input: CreateStaffInput): CreateStaffOutput {
        val response = client.mutation(
            AddStaffMutation(
                company = input.companyId,
                name = input.name,
                codename = input.codename,
                role = input.codename
            )
        ).execute()
        val staff = response.dataAssertNoErrors.company.addStaff.basicStaff
        return CreateStaffOutput(
            id = staff.id,
            codename = staff.codename,
            name = staff.data.name,
            role = staff.data.role,
        )
    }

    override suspend fun createStaffSchedule(input: CreateStaffScheduleInput): CreateStaffScheduleOutput {
        val response = client.mutation(
            EditStaffScheduleMutation(
                input.staffId,
                input.schedule.flatMap {
                    val list = mutableListOf<EditStaffScheduleInput>()
                    for (i in (it.start..it.end)) {
                        list.add(
                            EditStaffScheduleInput(
                                OperationType.EDIT,
                                date = i.toKotlinLocalDate(),
                                slots = listOf(
                                    EditStaffScheduleSlotInput(
                                        start = LocalTime(hour = 10, minute = 0),
                                        end = LocalTime(hour = 20, minute = 0)
                                    )
                                ),
                                company = it.companyId
                            )
                        )
                    }
                    list
                }
            )
        ).execute()
        return CreateStaffScheduleOutput(
            staffId = input.staffId,
            schedule = response.dataAssertNoErrors.staff.schedules.map {
                it.date.toJavaLocalDate()
            },
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