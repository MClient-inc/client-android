package ru.mclient.network.client

import com.apollographql.apollo3.ApolloClient
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import org.koin.core.annotation.Single
import ru.mclient.network.data.ClientAnalyticsWithCompanyQuery
import ru.mclient.network.data.ClientAnalyticsWithoutCompanyQuery
import ru.mclient.network.data.CreateClientForCompanyMutation
import ru.mclient.network.data.GetClientByIdQuery
import ru.mclient.network.data.GetClientCardByIdQuery
import ru.mclient.network.data.GetClientsForCompanyQuery
import ru.mclient.network.data.fragment.ClientAnalyticsFragment
import ru.mclient.network.data.fragment.ClientAnalyticsRecordFragment
import ru.mclient.network.data.type.DateRangeInput
import ru.mclient.network.data.type.RecordStatus.COME
import ru.mclient.network.data.type.RecordStatus.NOT_COME
import ru.mclient.network.data.type.RecordStatus.UNKNOWN__
import ru.mclient.network.data.type.RecordStatus.WAITING

@Single
class GraphQLClientNetworkSource(
    private val client: ApolloClient,
) : ClientNetworkSource {

    override suspend fun findClientsForCompany(input: GetClientsForCompanyInput): GetClientsForCompanyOutput {
        val data = client.query(GetClientsForCompanyQuery(input.companyId)).execute()
        return GetClientsForCompanyOutput(
            clients = data.dataAssertNoErrors.company.clients.map {
                GetClientsForCompanyOutput.Client(
                    id = it.basicClient.id,
                    title = it.basicClient.data.name,
                    phone = it.basicClient.data.phone?.clientPhoneNumber?.rawNumber.orEmpty(),
                )
            }
        )
    }

    override suspend fun getClientById(input: GetClientByIdInput): GetClientByIdOutput {
        val response = client.query(GetClientByIdQuery(input.clientId)).execute()
        val data = response.dataAssertNoErrors.client.basicClient
        return GetClientByIdOutput(
            id = data.id,
            name = data.data.name,
            phone = data.data.phone?.clientPhoneNumber?.rawNumber.orEmpty(),
        )
    }

    override suspend fun createClient(input: CreateClientInput): CreateClientOutput {
        val response = client.mutation(
            CreateClientForCompanyMutation(
                input.companyId,
                input.name,
                input.phone,
            )
        ).execute()
        val data = response.dataAssertNoErrors.company.addClient.basicClient
        return CreateClientOutput(
            id = data.id,
            name = data.data.name,
            phone = data.data.phone?.clientPhoneNumber?.rawNumber.orEmpty(),
        )
    }

    override suspend fun getClientCard(input: GetClientCardInput): GetClientCardOutput {
        val response = client.query(GetClientCardByIdQuery(input.clientId)).execute()
        return GetClientCardOutput(
            response.dataAssertNoErrors.client.card.token
        )
    }

    override suspend fun getClientAnalytics(input: GetClientAnalyticsInput): GetClientAnalyticsOutput {
        return if (input.companyId != null) {
            val response = client.query(
                ClientAnalyticsWithCompanyQuery(
                    input.clientId,
                    input.companyId!!,
                    DateRangeInput()
                )
            ).execute()
            val data = response.dataAssertNoErrors.client
            GetClientAnalyticsOutput(
                upcomingRecords = data.records.map {
                    it.clientAnalyticsRecordFragment.toData()
                },
                network = GetClientAnalyticsOutput.NetworkAnalytics(
                    data.network.id,
                    data.network.data.title,
                    data.analytics.forNetwork.counts.clientAnalyticsFragment.toData()
                ),
                company = GetClientAnalyticsOutput.CompanyAnalytics(
                    response.dataAssertNoErrors.company.id,
                    response.dataAssertNoErrors.company.data.title,
                    data.analytics.forCompany.counts.clientAnalyticsFragment.toData(),
                )
            )
        } else {
            val response = client.query(
                ClientAnalyticsWithoutCompanyQuery(
                    input.clientId,
                    DateRangeInput()
                )
            ).execute()
            val data = response.dataAssertNoErrors.client
            GetClientAnalyticsOutput(
                upcomingRecords = data.records.map {
                    it.clientAnalyticsRecordFragment.toData()
                },
                network = GetClientAnalyticsOutput.NetworkAnalytics(
                    data.network.id,
                    data.network.data.title,
                    data.analytics.forNetwork.counts.clientAnalyticsFragment.toData()
                ),
                company = null,
            )
        }
    }
}

private fun ClientAnalyticsFragment.toData(): GetClientAnalyticsOutput.ClientAnalyticsItem {
    return GetClientAnalyticsOutput.ClientAnalyticsItem(
        notComeCount = notComeCount.toLong(),
        comeCount = comeCount.toLong(),
        waitingCount = waitingCount.toLong(),
        totalCount = totalCount.toLong(),
    )
}

private fun ClientAnalyticsRecordFragment.toData(): GetClientAnalyticsOutput.Record {
    return GetClientAnalyticsOutput.Record(
        id = id,
        time = GetClientAnalyticsOutput.Time(
            date.start.date.toJavaLocalDate(),
            date.start.time.toJavaLocalTime(),
            date.end.time.toJavaLocalTime(),
        ),
        company = GetClientAnalyticsOutput.Company(
            company.id,
            company.data.title,
        ),
        staff = GetClientAnalyticsOutput.Staff(
            staff.id,
            staff.data.name,
        ),
        totalCost = info.totalCost,
        services = services.map {
            GetClientAnalyticsOutput.Service(
                id = it.id,
                title = it.data.title,
                cost = it.data.cost.rawCost,
            )
        },
        status = when (info.status) {
            WAITING -> GetClientAnalyticsOutput.RecordStatus.WAITING
            COME -> GetClientAnalyticsOutput.RecordStatus.COME
            NOT_COME -> GetClientAnalyticsOutput.RecordStatus.NOT_COME
            UNKNOWN__ -> GetClientAnalyticsOutput.RecordStatus.COME
        }
    )
}
