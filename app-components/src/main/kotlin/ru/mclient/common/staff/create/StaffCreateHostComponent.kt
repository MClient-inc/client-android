package ru.mclient.common.staff.create

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class StaffCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onSuccess: (Long) -> Unit,
) : StaffCreateHost, DIComponentContext by componentContext {


    override val bar: TopBar = ImmutableTopBar(TopBarState(title = "Создать работника"))

    override val staffCreate: StaffCreate = StaffCreateComponent(
        componentContext = childDIContext("staff_create"),
        companyId = companyId,
        onSuccess = onSuccess,
    )

}