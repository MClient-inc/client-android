package ru.mclient.common.staff.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab

class StaffListForCompanyHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelect: (Long) -> Unit,
    private val onCreate: () -> Unit,
) : StaffListHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(state = TopBarState("Работники"))

    override val list: StaffList =
        StaffListForCompanyComponent(
            componentContext = childDIContext(key = "staff_list"),
            companyId = companyId,
            onSelect = onSelect,
        )

    override val fab: Fab get() = ImmutableFab(list.state.toFabState(), onClick = onCreate)

    fun StaffListState.toFabState(): FabState {
        return FabState(
            title = "Добавить",
            isShown = !isLoading || staff.isNotEmpty(), isScrollInProgress = false
        )
    }

}