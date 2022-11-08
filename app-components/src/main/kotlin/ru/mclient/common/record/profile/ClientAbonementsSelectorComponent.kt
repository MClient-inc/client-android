package ru.mclient.common.record.profile

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.ClientAbonementsSelectorStore

class ClientAbonementsSelectorComponent(
    val componentContext: DIComponentContext,
) : ClientAbonementsSelector, DIComponentContext by componentContext {

    private val store: ClientAbonementsSelectorStore = getStore()

    override val state: ClientCreateAbonementsState by store.states(this) { it.toState() }

    private fun ClientAbonementsSelectorStore.State.toState(): ClientCreateAbonementsState {
        return ClientCreateAbonementsState(
            isExpanded = isExpanded,
            isAvailable = isAvailable,
            selectedAbonements = selectedAbonements.mapValues { (_, abonement) ->
                ClientCreateAbonementsState.ClientAbonement(
                    id = abonement.id,
                    usages = abonement.usages,
                    abonement = ClientCreateAbonementsState.Abonement(
                        id = abonement.abonement.id,
                        title = abonement.abonement.title,
                        subabonement = ClientCreateAbonementsState.Subabonement(
                            id = abonement.abonement.subabonement.id,
                            title = abonement.abonement.subabonement.title,
                            maxUsages = abonement.abonement.subabonement.maxUsages,
                            cost = abonement.abonement.subabonement.cost,
                        )
                    )
                )

            },
            clientAbonements = clientAbonements.map { abonement ->
                ClientCreateAbonementsState.ClientAbonement(
                    id = abonement.id,
                    usages = abonement.usages,
                    abonement = ClientCreateAbonementsState.Abonement(
                        id = abonement.abonement.id,
                        title = abonement.abonement.title,
                        subabonement = ClientCreateAbonementsState.Subabonement(
                            id = abonement.abonement.subabonement.id,
                            title = abonement.abonement.subabonement.title,
                            maxUsages = abonement.abonement.subabonement.maxUsages,
                            cost = abonement.abonement.subabonement.cost,
                        )
                    )
                )
            },
            isRefreshing = isRefreshing,
        )
    }

    override fun onDismiss() {
        store.accept(ClientAbonementsSelectorStore.Intent.Move(false))

    }

    override fun onExpand() {
        store.accept(ClientAbonementsSelectorStore.Intent.Move(true))
    }

    override fun onDelete(uniqueId: Int) {
        store.accept(ClientAbonementsSelectorStore.Intent.DeleteById(uniqueId))
    }

    override fun onSelect(clientAbonementId: Long) {
        store.accept(ClientAbonementsSelectorStore.Intent.Select(clientAbonementId))
    }

    override fun onRefresh() {
        store.accept(ClientAbonementsSelectorStore.Intent.Refresh)
    }


    fun setSelectedAbonements(clientId: Long?, abonements: List<Long>?) {
        store.accept(ClientAbonementsSelectorStore.Intent.ChangeClient(clientId, abonements))

    }

}