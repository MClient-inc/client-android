package ru.mclient.ui.servicecategory.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.servicecategory.create.ServiceCategoryCreate
import ru.mclient.common.servicecategory.create.ServiceCategoryCreateState
import ru.mclient.ui.view.toDesignedString

fun ServiceCategoryCreateState.toUI(): ServiceCategoryCreatePageState {
    return ServiceCategoryCreatePageState(
        title = title,
        isLoading = isLoading,
        error = if (isError) "Неизвестная ошибка".toDesignedString() else null
    )
}

@Composable
fun ServiceCategoryCreateUI(
    component: ServiceCategoryCreate,
    modifier: Modifier
) {
    ServiceCategoryCreatePage(
        state = component.state.toUI(),
        onUpdate = remember(component) { { component.onUpdate(it.title) } },
        onCreate = remember(component) { { component.onCreate(it.title) } },
        modifier = modifier
    )
}