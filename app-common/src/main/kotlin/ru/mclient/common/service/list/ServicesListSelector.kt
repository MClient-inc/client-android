package ru.mclient.common.service.list

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.mclient.common.servicecategory.list.ServiceCategoriesList

data class ServicesListSelectorState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val selectedServices: List<Service>,
) {
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
        val formattedCost: String,
        val uniqueId: Int,
    )
}

interface ServicesListSelector {

    val state: ServicesListSelectorState

    val childState: Value<ChildStack<*, Child>>

    fun onDismiss()

    fun onExpand()

    fun onDelete(serviceId: Int)


    sealed class Child {

        class CategoryList(val component: ServiceCategoriesList) : Child()

        class ServicesList(val component: ServiceList) : Child()

    }

}