package ru.mclient.network.analytics

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.format.DateTimeFormatter

@Single
class KtorAnalyticsNetworkSource(
    @Named("authorized")
    private val client: HttpClient,
) : AnalyticsNetworkSource {

    override suspend fun getCompanyAnalytics(input: GetCompanyAnalyticsInput): GetCompanyAnalyticsOutput {
        val response = client.get("/companies/${input.companyId}/analytics/main") {
            parameter("start", input.start.format(DateTimeFormatter.ISO_LOCAL_DATE))
            parameter("end", input.end.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
        val body = response.body<GetCompanyAnalyticsRequest>()
        return GetCompanyAnalyticsOutput(
            totalSum = body.totalSum,
            averageSum = body.averageSum,
            comeCount = body.comeCount,
            notComeCount = body.notComeCount,
            waitingCount = body.waitingCount,
        )
    }
}

@Serializable
class GetCompanyAnalyticsRequest(
    val totalSum: Long,
    val averageSum: Long,
    val comeCount: Int,
    val notComeCount: Int,
    val waitingCount: Int,
)