package ru.mclient.common.staff.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class StaffListForCompanyHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSelect: (Long) -> Unit,
    onCreate: () -> Unit,
) : StaffListHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(state = TopBarState("Работники"))

    override val list: StaffList =
        StaffListForCompanyComponent(
            componentContext = childDIContext(key = "staff_list"),
            companyId = companyId,
            onSelect = onSelect,
            onCreate = onCreate,
        )

}