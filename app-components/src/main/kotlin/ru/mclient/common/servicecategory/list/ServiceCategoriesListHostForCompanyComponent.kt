package ru.mclient.common.servicecategory.list

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ServiceCategoriesListHostForCompanyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
) : ServiceCategoriesListHost, DIComponentContext by componentContext {
    override val list: ServiceCategoriesList = ServiceCategoriesListForCompanyComponent(
        componentContext = childDIContext(key = "service_categories_list"),
        companyId = companyId
    )
    override val bar: TopBar = ImmutableTopBar(TopBarState("Категории"))

}