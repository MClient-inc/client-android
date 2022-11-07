package ru.mclient.network.abonnement

import java.time.LocalDateTime

data class GetAbonnementByIdInput(
    val abonnementId: Long
)

data class GetAbonnementByIdOutput(
    val abonnement: Abonnement
) {
    class Abonnement(
        val id: Long,
        val title: String,
//        val services: List<Service>,
        val subabonements: List<SubAbonnement>
    )

//    class Service(
//        val id: Long,
//        val title: String,
//        val cost: Long
//    )

    class SubAbonnement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime
    )
}