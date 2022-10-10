package ru.mclient.common.company.service.create

import kotlinx.coroutines.flow.StateFlow

data class ServiceCreateState(
    val serviceName : String,
    val description : String,
    val cost : Int,
    val isLoading : Boolean,
    val isError : Boolean
)

interface ServiceCreate {

    val state : StateFlow<ServiceCreateState>

    fun onUpdate(serviceName: String, description: String, cost: Int)

    fun onCreate(serviceName: String, description: String, cost: Int)

}