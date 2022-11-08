package ru.mclient.common.abonement.clientcreate

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.abonement.list.AbonementsList
import ru.mclient.common.abonement.list.AbonementsListComponent
import ru.mclient.common.childDIContext
import ru.mclient.common.utils.getStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.abonement.clientcreate.AbonementClientCreateAbonementSelectorStore

class AbonementClientCreateAbonementSelectorComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : AbonementClientCreateAbonementSelector, DIComponentContext by componentContext {

    private val store: AbonementClientCreateAbonementSelectorStore = getStore()

    override val state: AbonementClientCreateAbonementSelectorState by store.states(this) { it.toState() }

    private fun AbonementClientCreateAbonementSelectorStore.State.toState(): AbonementClientCreateAbonementSelectorState {
        return AbonementClientCreateAbonementSelectorState(
            isExpanded = isExpanded,
            isAvailable = isAvailable,
            abonement = abonement?.let { abonement ->
                AbonementClientCreateAbonementSelectorState.Abonement(
                    id = abonement.id,
                    title = abonement.title,
                    subabonement = AbonementClientCreateAbonementSelectorState.Subabonement(
                        id = abonement.subabonement.id,
                        title = abonement.subabonement.title,
                        cost = abonement.subabonement.cost,
                    )
                )
            },
            isSuccess = abonement != null,
        )
    }

    override val selector: AbonementsList = AbonementsListComponent(
        componentContext = childDIContext(key = "abonements_list"),
        companyId = companyId,
        isSubabonementClickable = true,
        onSelect = this::onSelect
    )

    private fun onSelect(item: AbonementsListComponent.Selected) {
        val abonement = item.abonement
        val subabonement = abonement.subabonement ?: return
        store.accept(
            AbonementClientCreateAbonementSelectorStore.Intent.Select(
                AbonementClientCreateAbonementSelectorStore.Intent.Select.Abonement(
                    id = abonement.id,
                    title = abonement.title,
                    subabonement = AbonementClientCreateAbonementSelectorStore.Intent.Select.Subabonement(
                        id = subabonement.id,
                        title = subabonement.title,
                        cost = subabonement.cost,
                    )
                )
            )
        )
    }

    override fun onDismiss() {
        store.accept(AbonementClientCreateAbonementSelectorStore.Intent.Move(false))
    }

    override fun onExpand() {
        store.accept(AbonementClientCreateAbonementSelectorStore.Intent.Move(true))
    }
}