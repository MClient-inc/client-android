package ru.mclient.common.service.create

data class ServiceCreateState(
    val title: String,
    val description: String,
    val cost: String,
    val isLoading: Boolean,
    val isError: Boolean
)

interface ServiceCreate {

    val state: ServiceCreateState

    fun onUpdate(title: String, description: String, cost: String)

    fun onCreate(title: String, description: String, cost: String)

}