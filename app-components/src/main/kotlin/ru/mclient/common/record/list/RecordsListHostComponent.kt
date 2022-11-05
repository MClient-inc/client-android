package ru.mclient.common.record.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class RecordsListHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onRecordCreate: () -> Unit,
) : RecordsListHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Записи"))

    override val recordsList: RecordsList = RecordsListComponent(
        componentContext = childDIContext(key = "records_list"),
        companyId = companyId,
    )

    override fun onRecordCreate() {
        onRecordCreate.invoke()
    }

}