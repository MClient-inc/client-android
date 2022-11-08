package ru.mclient.mvi.record.create

import com.arkivanov.mvikotlin.core.store.Store

interface ClientAbonementsSelectorStore :
    Store<ClientAbonementsSelectorStore.Intent, ClientAbonementsSelectorStore.State, ClientAbonementsSelectorStore.Label> {

    sealed class Intent {

        class Select(val abonementId: Long) :
            Intent()

        class ChangeClient(val clientId: Long?, val abonements: List<Long>?) : Intent()

        class DeleteById(val id: Int) : Intent()

        class Move(val isExpanded: Boolean) : Intent()

        object Refresh : Intent()

    }

    data class State(
        val clientId: Long?,
        val isLoading: Boolean,
        val isExpanded: Boolean,
        val isAvailable: Boolean,
        val selectedAbonements: Map<Int, ClientAbonement>,
        val clientAbonements: List<ClientAbonement>,
        val isRefreshing: Boolean,
    ) {

        data class ClientAbonement(
            val id: Long,
            val usages: Int,
            val abonement: Abonement,
        )

        data class Abonement(
            val id: Long,
            val title: String,
            val subabonement: Subabonement,
        )

        data class Subabonement(
            val id: Long,
            val title: String,
            val cost: Long,
            val maxUsages: Int,
        )
    }

    sealed class Label

}