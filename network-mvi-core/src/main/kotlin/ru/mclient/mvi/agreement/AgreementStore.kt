package ru.mclient.mvi.agreement

import ru.mclient.mvi.ParametrizedStore

interface AgreementStore :
    ParametrizedStore<AgreementStore.Intent, AgreementStore.State, AgreementStore.Label, AgreementStore.Param> {

    data class Param(val type: AgreementType, val loadOnStart: Boolean = false) {

        enum class AgreementType {
            USER_AGREEMENT, CLIENT_DATA_PROCESSING_AGREEMENT,
        }

    }

    sealed class Intent {

        object Load : Intent()

    }

    data class State(
        val title: String?,
        val content: String?,
        val isLoaded: Boolean,
        val isError: Boolean,
    )

    sealed class Label

}