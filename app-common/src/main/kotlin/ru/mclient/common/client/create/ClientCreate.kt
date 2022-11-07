package ru.mclient.common.client.create

data class ClientCreateState(
    val name: String,
    val phone: String,
    val isLoading: Boolean,
    val error: String?,
)

interface ClientCreate {

    val state: ClientCreateState

    fun onUpdate(name: String, phone: String)

    fun onCreate()

}