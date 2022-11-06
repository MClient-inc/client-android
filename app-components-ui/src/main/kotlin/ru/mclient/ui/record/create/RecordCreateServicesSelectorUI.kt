package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.record.create.RecordCreateServicesSelector
import ru.mclient.common.record.create.RecordCreateServicesSelectorState
import ru.mclient.ui.service.list.ServiceListUI
import ru.mclient.ui.servicecategory.list.ServiceCategoriesListUI

@Composable
fun RecordCreateServicesSelectorUI(
    component: RecordCreateServicesSelector,
    modifier: Modifier,
) {
    RecordCreateServicesBlock(
        state = component.state.toUI(),
        onExpand = component::onExpand,
        onDismiss = component::onDismiss,
        onDelete = { component.onDelete(it.uniqueId) },
        modifier = modifier,
    ) {
        Children(stack = component.childState) {
            RecordCreateServicesSelectorNavHost(
                child = it.instance,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun RecordCreateServicesSelectorState.toUI(): RecordCreateServicesBlockState {
    return RecordCreateServicesBlockState(
        isExpanded = isExpanded,
        isAvailable = isAvailable,
        selectedServices = selectedServices.map {
            RecordCreateServicesBlockState.Service(
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
fun RecordCreateServicesSelectorNavHost(
    child: RecordCreateServicesSelector.Child,
    modifier: Modifier,
) {
    when (child) {
        is RecordCreateServicesSelector.Child.CategoryList ->
            ServiceCategoriesListUI(
                component = child.component,
                modifier = modifier,
            )

        is RecordCreateServicesSelector.Child.ServicesList ->
            ServiceListUI(
                component = child.component,
                modifier = modifier,
            )
    }
}