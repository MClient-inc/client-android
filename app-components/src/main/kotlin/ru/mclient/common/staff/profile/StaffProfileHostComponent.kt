package ru.mclient.common.staff.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class StaffProfileHostComponent(
    componentContext: DIComponentContext,
    staffId: Long,
    companyId: Long,
    onEditSchedule: () -> Unit,
) : StaffProfileHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Работник"))

    override val profile: StaffProfile =
        StaffProfileComponent(
            componentContext = childDIContext(key = "staff_profile"),
            staffId = staffId,
            companyId = companyId,
            onEditSchedule = onEditSchedule
        )

}