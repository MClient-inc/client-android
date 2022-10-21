package ru.mclient.ui.servicecategory.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mclient.common.servicecategory.list.ServiceCategoriesList
import ru.mclient.common.servicecategory.list.ServiceCategoriesListState

fun ServiceCategoriesListState.toUI(): ServiceCategoriesListPageState {
    return ServiceCategoriesListPageState(
        companies = categories.map(ServiceCategoriesListState.ServiceCategory::toUI),
        isLoading = isLoading,
        isRefreshing = isRefreshing,
    )
}

fun ServiceCategoriesListState.ServiceCategory.toUI(): ServiceCategoriesListPageState.ServiceCategory {
    return ServiceCategoriesListPageState.ServiceCategory(
        id = id,
        title = title,
    )
}

@Composable
fun ServiceCategoriesListUI(component: ServiceCategoriesList, modifier: Modifier) {
    val state by component.categories
    ServiceCategoriesListPage(
        state = state.toUI(),
        onRefresh = component::onRefresh,
        onSelect = remember(component) { { component.onCategory(it.id) } },
        modifier = modifier,
    )
}