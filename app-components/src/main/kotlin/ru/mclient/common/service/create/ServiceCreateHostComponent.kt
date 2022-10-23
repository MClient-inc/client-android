package ru.mclient.common.service.create

import ru.mclient.common.DIComponentContext
import ru.mclient.common.bar.ImmutableTopBar
import ru.mclient.common.bar.TopBar
import ru.mclient.common.bar.TopBarState
import ru.mclient.common.childDIContext

class ServiceCreateHostComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    categoryId: Long,
    onCreated: (CreatedService) -> Unit,
) : ServiceCreateHost, DIComponentContext by componentContext {

    override val bar: TopBar = ImmutableTopBar(TopBarState("Создание услуги"))

    override val serviceCreate: ServiceCreate = ServiceCreateComponent(
        componentContext = childDIContext(key = "service_create"),
        companyId = companyId,
        categoryId = categoryId,
        onCreated = { onCreated.invoke(CreatedService(it.id)) }
    )

    @JvmInline
    value class CreatedService(
        val id: Long,
    )
}