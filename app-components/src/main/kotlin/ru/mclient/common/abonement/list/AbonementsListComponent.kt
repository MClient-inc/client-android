package ru.mclient.common.abonement.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.list.AbonementsListStore

class AbonementsListComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val isAbonementClickable: Boolean = false,
    private val isSubabonementClickable: Boolean = false,
    private val onSelect: (Selected) -> Unit = {},
) : AbonementsList, DIComponentContext by componentContext {

    private val store: AbonementsListStore =
        getParameterizedStore { AbonementsListStore.Params(companyId) }

    override val state: AbonementsListState by store.states(this) { it.toState() }

    private fun AbonementsListStore.State.toState(): AbonementsListState {
        return AbonementsListState(
            abonements = abonements.map { abonement ->
                AbonementsListState.Abonement(
                    abonement.id,
                    abonement.title,
                    abonement.subabonements.map {
                        AbonementsListState.Subabonement(it.id, it.title, it.cost)
                    }
                )
            },
            isAbonementClickable = isAbonementClickable,
            isSubabonementClickable = isSubabonementClickable,
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            isFailure = isFailure
        )
    }

    override fun onRefresh() {
        store.accept(AbonementsListStore.Intent.Refresh)
    }

    override fun onSelect(abonementId: Long) {
        val abonement = state.abonements.firstOrNull { it.id == abonementId } ?: return
        onSelect.invoke(
            Selected(
                Selected.Abonement(
                    id = abonement.id,
                    title = abonement.title,
                    subabonement = null,
                )
            )
        )
    }

    override fun onSelect(abonementId: Long, subabonementId: Long) {
        val abonement = state.abonements.firstOrNull { it.id == abonementId } ?: return
        val subabonement = abonement.subabonements.firstOrNull { it.id == subabonementId } ?: return
        onSelect.invoke(
            Selected(
                Selected.Abonement(
                    id = abonement.id,
                    title = abonement.title,
                    subabonement = Selected.Subabonement(
                        id = subabonement.id,
                        title = subabonement.title,
                        cost = subabonement.cost
                    )
                )
            )
        )
    }

    class Selected(
        val abonement: Abonement,
    ) {
        data class Abonement(
            val id: Long,
            val title: String,
            val subabonement: Subabonement?,
        )

        data class Subabonement(
            val id: Long,
            val title: String,
            val cost: Long,
        )
    }

}