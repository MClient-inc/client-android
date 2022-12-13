package ru.mclient.common.staff.schedule

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState

class StaffScheduleHostComponent(
    componentContext: DIComponentContext,
    staffId: Long,
    companyId: Long,
    onSuccess: () -> Unit,
) : StaffScheduleHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("График рабботы"))

    override val staffSchedule: StaffSchedule = StaffScheduleComponent(
        componentContext = componentContext,
        staffId = staffId,
        companyId = companyId,
        onSuccess = onSuccess
    )
}