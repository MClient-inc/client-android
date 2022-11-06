package ru.mclient.common.servicecategory.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab

class ServiceCategoriesListHostForCompanyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onCategorySelected: (Long) -> Unit,
    private val onCreate: () -> Unit,
) : ServiceCategoriesListHost, DIComponentContext by componentContext {

    override val list: ServiceCategoriesList = ServiceCategoriesListForCompanyComponent(
        componentContext = childDIContext(key = "service_categories_list"),
        companyId = companyId,
        onCategorySelected = onCategorySelected,
    )

    override val fab: Fab
        get() = ImmutableFab(
            FabState(
                title = "Добавить",
                isShown = !list.state.isLoading || list.state.categories.isNotEmpty(),
                isScrollInProgress = false,
            ), onCreate
        )

    override val bar: TopBar = ImmutableTopBar(TopBarState("Категории"))

}