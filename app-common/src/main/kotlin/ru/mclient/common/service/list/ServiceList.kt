package ru.mclient.common.service.list

class ServiceListState(
    val services: List<Service>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
        val formattedCost: String,
    )
}

interface ServiceList {

    val state: ServiceListState

    fun onRefresh()

    fun onService(serviceId: Long)


}