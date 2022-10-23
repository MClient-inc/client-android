package ru.mclient.ui.servicecategory.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun ServiceCategoryCreateHostUI(
    component: ServiceCategoryCreateHost,
    modifier: Modifier
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        ServiceCategoryCreateUI(
            component = component.categoryCreate,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
        )
    }
}