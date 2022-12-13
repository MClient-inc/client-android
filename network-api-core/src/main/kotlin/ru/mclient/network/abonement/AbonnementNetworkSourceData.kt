package ru.mclient.network.abonement

import java.time.LocalDateTime

data class CreateAbonementInput(
    val companyId: String,
    val title: String,
    val subabonements: List<Subabonement>,
    val services: List<String>,
) {

    class Subabonement(
        val title: String,
        val usages: Int,
        val cost: Long,
    )

}

data class CreateAbonementOutput(
    val abonement: Abonement,
) {
    class Abonement(
        val id: String,
        val title: String,
        val services: List<Service>,
        val subabonements: List<Subabonement>,
    )

    class Service(
        val id: String,
        val title: String,
        val cost: Long,
    )

    class Subabonement(
        val id: String,
        val title: String,
        val usages: Int,
        val cost: Long,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )
}

data class GetAbonementByIdInput(
    val abonementId: String,
)

data class GetAbonementsForCompanyInput(
    val companyId: String,
)

data class GetAbonementsForCompanyOutput(
    val abonements: List<Abonement>,
) {
    class Abonement(
        val id: String,
        val title: String,
        val subabonements: List<Subabonement>,
    )

    class Subabonement(
        val id: String,
        val title: String,
        val cost: Long,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )
}

data class GetAbonementsForClientInput(
    val clientId: String,
)


data class AddAbonementToClientInput(
    val clientId: String,
    val subabonementId: String,
)

data class AddAbonementToClientOutput(
    val clientId: String,
)

data class GetAbonementsForClientOutput(
    val abonements: List<ClientAbonement>,
) {

    class ClientAbonement(
        val id: String,
        val usages: Int,
        val abonement: Abonement,
    )

    class Abonement(
        val id: String,
        val title: String,
        val subabonement: Subabonement,
    )

    class Subabonement(
        val id: String,
        val title: String,
        val cost: Long,
        val maxUsages: Int,
    )

}

data class GetAbonementByIdOutput(
    val abonement: Abonement,
) {
    class Abonement(
        val id: String,
        val title: String,
        val services: List<Service>,
        val subabonements: List<Subabonement>,
    )

    class Service(
        val id: String,
        val title: String,
        val cost: Long,
    )

    class Subabonement(
        val id: String,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )

}