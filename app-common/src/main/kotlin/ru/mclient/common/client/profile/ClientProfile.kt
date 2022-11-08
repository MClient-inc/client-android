package ru.mclient.common.client.profile

class ClientProfileState(
    val client: Client?,
    val abonements: List<ClientAbonement>?,
    val isLoading: Boolean,
) {
    class Client(
        val phone: String,
        val name: String,
    )

    class ClientAbonement(
        val id: Long,
        val usages: Int,
        val abonement: Abonement,
    )

    class Abonement(
        val title: String,
        val subabonement: Subabonement,
    )

    class Subabonement(
        val title: String,
        val maxUsages: Int,
    )

}

interface ClientProfile {

    val state: ClientProfileState

    fun onRefresh()

    fun onEdit()

    fun onAbonementCreate()

    fun onQRCode()

}