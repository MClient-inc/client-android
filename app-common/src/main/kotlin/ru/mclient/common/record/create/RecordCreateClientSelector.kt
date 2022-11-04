package ru.mclient.common.record.create

import ru.mclient.common.client.list.ClientsList

data class RecordCreateClientSelectorState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val isSuccess: Boolean,
    val selectedClient: Client?,
) {
    class Client(
        val id: Long,
        val name: String,
    )
}

interface RecordCreateClientSelector {

    val state: RecordCreateClientSelectorState

    val clientsList: ClientsList

    fun onDismiss()

    fun onExpand()

}