package ru.mclient.common.record.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.childDIContext
import ru.mclient.common.client.list.ClientsList
import ru.mclient.common.client.list.ClientsListForCompanyComponent
import ru.mclient.common.utils.getStoreSavedState
import ru.mclient.common.utils.states
import ru.mclient.mvi.record.create.RecordCreateClientSelectorStore

class RecordCreateClientSelectorComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : RecordCreateClientSelector, DIComponentContext by componentContext {

    private val store: RecordCreateClientSelectorStore = getStoreSavedState("record_create_clients")

    override val state: RecordCreateClientSelectorState by store.states(this) { it.toState() }

    private fun RecordCreateClientSelectorStore.State.toState(): RecordCreateClientSelectorState {
        return RecordCreateClientSelectorState(
            isExpanded = isExpanded,
            isAvailable = isAvailable,
            isSuccess = selectedClient != null,
            selectedClient = selectedClient?.let {
                RecordCreateClientSelectorState.Client(
                    id = it.id,
                    name = it.name
                )
            }
        )
    }

    override val clientsList: ClientsList = ClientsListForCompanyComponent(
        componentContext = childDIContext(key = "clients_list"),
        companyId = companyId,
        onClient = { onSelect(it.id, it.name) },
    )

    override fun onDismiss() {
        store.accept(RecordCreateClientSelectorStore.Intent.Move(false))
    }

    override fun onExpand() {
        store.accept(RecordCreateClientSelectorStore.Intent.Move(true))
    }

    private fun onSelect(id: Long, name: String) {
        store.accept(RecordCreateClientSelectorStore.Intent.Select(id, name))
    }

}