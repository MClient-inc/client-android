package ru.mclient.common.service.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.MutableTopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext
import ru.mclient.common.fab.Fab
import ru.mclient.common.fab.FabState
import ru.mclient.common.fab.ImmutableFab

class ServiceListForCategoryAndCompanyHostComponent(
    componentContext: DIComponentContext,
    categoryId: Long,
    companyId: Long,
    private val onCreate: () -> Unit,
    onSelect: (Long) -> Unit,
) : ServiceListHost, DIComponentContext by componentContext {

    override val bar = MutableTopBar(
        TopBarState(
            title = "Услуги категории",
            isLoading = true,
        )
    )

    override val list: ServiceList = ServiceListForCategoryAndCompanyComponent(
        componentContext = childDIContext(key = "service_list"),
        categoryId = categoryId,
        companyId = companyId,
        onCategoryTitle = this::onCategoryTitle,
        onSelect = { onSelect(it.id) },
    )

    override val fab: Fab
        get() = ImmutableFab(
            FabState(
                title = "Добавить",
                isShown = !list.state.isLoading || list.state.services.isNotEmpty(),
                isScrollInProgress = false,
            ),
            onClick = onCreate,
        )

    private fun onCategoryTitle(title: String?) {
        if (title != null) {
            bar.update { TopBarState(title, false) }
        } else {
            bar.update { it.copy(isLoading = true) }
        }
    }
}