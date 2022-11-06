package ru.mclient.ui.servicecategory.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.servicecategory.list.ServiceCategoriesListHost
import ru.mclient.ui.bar.MergedHostUI

@Composable
fun ServiceCategoriesListHostUI(
    component: ServiceCategoriesListHost,
    modifier: Modifier,
) {
    MergedHostUI(component = component, modifier = modifier) {
        ServiceCategoriesListUI(
            component = component.list,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}