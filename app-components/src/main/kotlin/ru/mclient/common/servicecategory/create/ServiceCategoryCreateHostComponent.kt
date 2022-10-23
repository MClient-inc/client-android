package ru.mclient.common.servicecategory.create

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ServiceCategoryCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    onCreated: (CreatedCategory) -> Unit
) : ServiceCategoryCreateHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Создание категории услуг"))
    override val categoryCreate: ServiceCategoryCreate = ServiceCategoryCreateComponent(
        componentContext = childDIContext(key = "service_category_create"),
        companyId = companyId,
        onCreated = { onCreated(CreatedCategory(it.id)) },
    )

    @JvmInline
    value class CreatedCategory(
        val id: Long,
    )

}