package ru.mclient.common.servicecategory.list

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

    val state: ServiceCategoriesListState

    fun onRefresh()

    fun onCategory(categoryId: Long)

}