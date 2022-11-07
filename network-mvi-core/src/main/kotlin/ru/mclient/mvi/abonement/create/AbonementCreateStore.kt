package ru.mclient.mvi.abonement.create

import ru.mclient.mvi.ParametrizedStore

interface AbonementCreateStore :
    ParametrizedStore<AbonementCreateStore.Intent, AbonementCreateStore.State, AbonementCreateStore.Label, AbonementCreateStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        class Create(
            val title: String,
            val subabonements: List<Subabonement>,
            val services: List<Long>,
        ) : Intent() {

            data class Subabonement(
                val title: String,
                val usages: Int,
            )

        }

    }

    data class State(
        val isAvailable: Boolean,
        val isLoading: Boolean,
        val isSuccess: Boolean,
        val abonement: Abonement?,
    ) {
        class Abonement(
            val id: Long,
        )
    }

    sealed class Label

}