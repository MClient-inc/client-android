package ru.mclient.network.abonement

import java.time.LocalDateTime

data class CreateAbonementInput(
    val companyId: Long,
    val title: String,
    val subabonements: List<Subabonement>,
    val services: List<Long>,
) {

    class Subabonement(
        val title: String,
        val usages: Int,
    )

}

data class CreateAbonementOutput(
    val abonement: Abonement,
) {
    class Abonement(
        val id: Long,
        val title: String,
        val services: List<Service>,
        val subabonements: List<Subabonement>,
    )

    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )
}

data class GetAbonementByIdInput(
    val abonementId: Long,
)

data class GetAbonementsForCompanyInput(
    val companyId: Long,
)

data class GetAbonementsForCompanyOutput(
    val abonements: List<Abonement>,
) {
    class Abonement(
        val id: Long,
        val title: String,
        val subabonements: List<Subabonement>,
    )

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )
}

data class GetAbonementByIdOutput(
    val abonement: Abonement,
) {
    class Abonement(
        val id: Long,
        val title: String,
        val services: List<Service>,
        val subabonements: List<Subabonement>,
    )

    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )

}