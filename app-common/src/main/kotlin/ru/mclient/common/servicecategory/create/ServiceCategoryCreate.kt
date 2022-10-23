package ru.mclient.common.servicecategory.create

data class ServiceCategoryCreateState(
    val title: String,
    val isLoading: Boolean,
    val isError: Boolean
)

interface ServiceCategoryCreate {

    val state: ServiceCategoryCreateState

    fun onUpdate(title: String)

    fun onCreate(title: String)

}