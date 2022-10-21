package ru.mclient.common.servicecategory.list

import androidx.compose.runtime.State

class ServiceCategoriesListState(
    val categories: List<ServiceCategory>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class ServiceCategory(
        val id: Long,
        val title: String,
    )
}

interface ServiceCategoriesList {

    val categories: State<ServiceCategoriesListState>

    fun onRefresh()

    fun onCategory(serviceId: Long)

}