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
            totalSum = body.totalSum.toItem(),
            averageSum = body.averageSum.toItem(),
            comeCount = body.comeCount.toItem(),
            notComeCount = body.notComeCount.toItem(),
            waitingCount = body.waitingCount.toItem(),
        )
    }
}

private fun AnalyticItemRequest.toItem(): AnalyticItem
{
    return AnalyticItem(value, difference)
}
@Serializable
class GetCompanyAnalyticsRequest(
    val totalSum: AnalyticItemRequest,
    val averageSum: AnalyticItemRequest,
    val comeCount: AnalyticItemRequest,
    val notComeCount: AnalyticItemRequest,
    val waitingCount: AnalyticItemRequest,
)

@Serializable
class AnalyticItemRequest(
    val value: String,
    val difference: Int,
)