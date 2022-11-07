package ru.mclient.common.abonement.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.list.AbonementsListStore

class AbonementsListComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onSelect: (Long) -> Unit,
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
                        AbonementsListState.Subabonement(it.title)
                    }
                )
            },
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            isFailure = isFailure
        )
    }

    override fun onRefresh() {
        store.accept(AbonementsListStore.Intent.Refresh)
    }

    override fun onSelect(abonementId: Long) {
        onSelect.invoke(abonementId)
    }
}