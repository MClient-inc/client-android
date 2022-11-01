package ru.mclient.common.service.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.MutableTopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ServiceListForCategoryAndCompanyHostComponent(
    componentContext: DIComponentContext,
    categoryId: Long,
    companyId: Long,
    onCreate: () -> Unit,
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
        onCreate = onCreate,
        onSelect = onSelect,
    )

    private fun onCategoryTitle(title: String?) {
        if (title != null) {
            bar.update { TopBarState(title, false) }
        } else {
            bar.update { it.copy(isLoading = true) }
        }
    }
}