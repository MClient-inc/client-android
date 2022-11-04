package ru.mclient.common.client.list

class ClientsListState(
    val clients: List<Client>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )
}

interface ClientsList {

    val state: ClientsListState

    fun onRefresh()

    fun onClient(clientId: Long, name: String)

}