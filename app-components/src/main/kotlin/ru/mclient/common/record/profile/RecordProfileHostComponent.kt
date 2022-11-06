package ru.mclient.common.record.profile

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class RecordProfileHostComponent(
    componentContext: DIComponentContext,
    recordId: Long
) : RecordProfileHost, DIComponentContext by componentContext {

    override val bar: TopBar =
        ImmutableTopBar(TopBarState("Запись"))

    override val recordProfile: RecordProfile =
        RecordProfileComponent(
            componentContext = childDIContext(key = "record_profile"),
            recordId = recordId
        )
}