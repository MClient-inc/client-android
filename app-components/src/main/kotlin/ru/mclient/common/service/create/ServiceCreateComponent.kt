package ru.mclient.common.service.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.service.create.ServiceCreateStore

class ServiceCreateComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    categoryId: Long,
    private val onCreated: (CreatedService) -> Unit,
) : ServiceCreate, DIComponentContext by componentContext {

    private val store: ServiceCreateStore =
        getParameterizedStore {
            ServiceCreateStore.Params(
                companyId = companyId,
                categoryId = categoryId
            )
        }

    override val state: ServiceCreateState by store.states(this) { it.toState() }

    private fun ServiceCreateStore.State.toState(): ServiceCreateState {
        val service = service
        if (isSuccess && service != null) {
            onCreated.invoke(CreatedService(service.id))
        }
        return ServiceCreateState(
            title = title,
            description = description,
            cost = cost,
            isLoading = isLoading,
            isError = isError
        )
    }

    override fun onUpdate(title: String, description: String, cost: String) {
        store.accept(ServiceCreateStore.Intent.Update(title, description, cost))
    }

    override fun onCreate(title: String, description: String, cost: String) {
        store.accept(ServiceCreateStore.Intent.Create)
    }

    @JvmInline
    value class CreatedService(
        val id: Long,
    )

}