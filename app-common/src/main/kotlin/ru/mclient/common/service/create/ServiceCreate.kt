package ru.mclient.common.service.create

data class ServiceCreateState(
    val serviceName: String,
    val description: String,
    val cost: String,
    val isLoading: Boolean,
    val isError: Boolean
)

interface ServiceCreate {

    val state: ServiceCreateState

    fun onUpdate(serviceName: String, description: String, cost: String)

    fun onCreate(serviceName: String, description: String, cost: String)

}