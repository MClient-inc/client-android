package ru.mclient.network.analytics

import java.time.LocalDate

class GetCompanyAnalyticsInput(
    val companyId: Long,
    val start: LocalDate,
    val end: LocalDate,
)

class GetCompanyAnalyticsOutput(
    val totalSum: AnalyticItem,
    val averageSum: AnalyticItem,
    val comeCount: AnalyticItem,
    val notComeCount: AnalyticItem,
    val waitingCount: AnalyticItem,
)

class AnalyticItem(
    val value: String,
    val difference: Int,
)