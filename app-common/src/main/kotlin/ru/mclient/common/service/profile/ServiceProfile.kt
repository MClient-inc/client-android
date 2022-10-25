package ru.mclient.common.service.profile

data class ServiceProfileState(
    val service: Service?,
    val isLoading: Boolean
) {
    data class Service(
        val title: String,
        val cost: String,
        val description: String
    )
}

interface ServiceProfile {

    val state: ServiceProfileState

    fun onRefresh()

    fun onEdit()

}