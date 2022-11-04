package ru.mclient.common.client.profile

class ClientProfileState(
    val client: Client?,
    val isLoading: Boolean,
) {
    class Client(
        val phone: String,
        val name: String,
    )
}

interface ClientProfile {

    val state: ClientProfileState

    fun onRefresh()

    fun onEdit()

}