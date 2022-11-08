package ru.mclient.common.abonement.clientcreate

class AbonementClientCreateClientState(
    val client: Client?,
    val isLoading: Boolean,
) {
    class Client(
        val name: String,
        val phone: String,
    )
}

interface AbonementClientCreateClient {

    val state: AbonementClientCreateClientState

}