package ru.mclient.ui.service.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.service.list.ServicesListSelector
import ru.mclient.common.service.list.ServicesListSelectorState
import ru.mclient.ui.servicecategory.list.ServiceCategoriesListUI

@Composable
fun ServicesListSelectorUI(
    component: ServicesListSelector,
    modifier: Modifier,
) {
    ServicesListSelectorBlock(
        state = component.state.toUI(),
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        onDelete = { component.onDelete(it.uniqueId) },
        modifier = modifier,
    ) {
        Children(stack = component.childState) {
            ServicesListSelectorNavHost(
                child = it.instance,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun ServicesListSelectorState.toUI(): ServicesListSelectorBlockState {
    return ServicesListSelectorBlockState(
        isExpanded = isExpanded,
        isAvailable = isAvailable,
        selectedServices = selectedServices.map {
            ServicesListSelectorBlockState.Service(
                id = it.id,
                title = it.title,
                cost = it.cost,
                formattedCost = it.formattedCost,
                uniqueId = it.uniqueId,
            )
        }
    )
}

@Composable
fun ServicesListSelectorNavHost(
    child: ServicesListSelector.Child,
    modifier: Modifier,
) {
    when (child) {
        is ServicesListSelector.Child.CategoryList ->
            ServiceCategoriesListUI(
                component = child.component,
                modifier = modifier,
            )

        is ServicesListSelector.Child.ServicesList ->
            ServiceListUI(
                component = child.component,
                modifier = modifier,
            )
    }
}