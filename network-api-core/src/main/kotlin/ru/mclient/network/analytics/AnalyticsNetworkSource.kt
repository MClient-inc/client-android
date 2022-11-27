package ru.mclient.network.analytics

interface AnalyticsNetworkSource {

    suspend fun getCompanyAnalytics(input: GetCompanyAnalyticsInput): GetCompanyAnalyticsOutput

}