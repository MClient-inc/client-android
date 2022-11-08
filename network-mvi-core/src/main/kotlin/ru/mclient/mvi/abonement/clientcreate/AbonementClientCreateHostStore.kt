package ru.mclient.mvi.abonement.clientcreate

import ru.mclient.mvi.ParametrizedStore

interface AbonementClientCreateHostStore :
    ParametrizedStore<AbonementClientCreateHostStore.Intent, AbonementClientCreateHostStore.State, AbonementClientCreateHostStore.Label, AbonementClientCreateHostStore.Params> {

    data class Params(
        val companyId: Long,
        val clientId: Long,
    )

    sealed class Intent {

        class Create(
            val subabonementId: Long,
        ) : Intent()

    }

    data class State(
        val isAvailable: Boolean,
        val isLoading: Boolean,
        val isSuccess: Boolean,
    )

    sealed class Label

}