package ru.mclient.mvi.abonement.profile

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDateTime

interface AbonementProfileStore :
    ParametrizedStore<AbonementProfileStore.Intent, AbonementProfileStore.State, AbonementProfileStore.Label, AbonementProfileStore.Params> {

    @JvmInline
    value class Params(
        val abonementId: Long,
    )

    sealed class Intent {
        object Refresh : Intent()
    }

    data class State(
        val abonement: Abonement?,
        val isFailure: Boolean,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) {

        data class Abonement(
            val id: Long,
            val title: String,
            val subabonements: List<Subabonement>,
            val services: List<Service>,
        )

        data class Subabonement(
            val id: Long,
            val title: String,
            val availableUntil: LocalDateTime,
            val liveTimeInMillis: Long,
            val usages: Int,
        )

        data class Service(
            val id: Long,
            val title: String,
            val cost: Long
        )


    }

    sealed class Label

}