package ru.mclient.network.analytics

import java.time.LocalDate

class GetCompanyAnalyticsInput(
    val companyId: Long,
    val start: LocalDate,
    val end: LocalDate,
)

class GetCompanyAnalyticsOutput(
    val totalSum: Long,
    val averageSum: Long,
    val comeCount: Int,
    val notComeCount: Int,
    val waitingCount: Int,
)