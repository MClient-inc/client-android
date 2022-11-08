package ru.mclient.common.abonement.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab

class AbonementsListHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelect: (Long) -> Unit,
    private val onCreate: () -> Unit,
) : AbonementsListHost, DIComponentContext by componentContext {

    override val abonementsList: AbonementsList = AbonementsListComponent(
        componentContext = childDIContext(key = "abonements_list"),
        companyId = companyId,
        isAbonementClickable = true,
        onSelect = { onSelect(it.abonement.id) },
    )

    override val bar: TopBar = ImmutableTopBar(TopBarState("Абонементы"))

    override val fab: Fab
        get() = ImmutableFab(
            FabState(
                title = "Добавить",
                isShown = !abonementsList.state.isLoading || abonementsList.state.abonements.isNotEmpty(),
            ),
            onClick = onCreate,
        )

}