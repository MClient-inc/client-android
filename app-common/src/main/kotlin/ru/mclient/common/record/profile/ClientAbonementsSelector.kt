package ru.mclient.common.record.profile

data class ClientCreateAbonementsState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val isRefreshing: Boolean,
    val selectedAbonements: Map<Int, ClientAbonement>,
    val clientAbonements: List<ClientAbonement>,
) {
    class ClientAbonement(
        val id: Long,
        val abonement: Abonement,
        val usages: Int,
    )

    data class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )

    data class Subabonement(
        val id: Long,
        val title: String,
        val maxUsages: Int,
        val cost: Long,
    )

}

interface ClientAbonementsSelector {

    val state: ClientCreateAbonementsState

    fun onDismiss()

    fun onExpand()

    fun onDelete(uniqueId: Int)

    fun onSelect(clientAbonementId: Long)

    fun onRefresh()

}