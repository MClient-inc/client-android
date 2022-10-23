package ru.mclient.common.servicecategory.create

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.servicecategory.create.ServiceCategoryCreateStore

class ServiceCategoryCreateComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onCreated: (CreatedCategory) -> Unit,
) : ServiceCategoryCreate, DIComponentContext by componentContext {

    private val store: ServiceCategoryCreateStore =
        getParameterizedStore { ServiceCategoryCreateStore.Params(companyId) }

    override val state: ServiceCategoryCreateState by store.states(this) { it.toState() }

    private fun ServiceCategoryCreateStore.State.toState(): ServiceCategoryCreateState {
        val category = category
        if (isSuccess && category != null) {
            onCreated.invoke(CreatedCategory(category.id))
        }
        return ServiceCategoryCreateState(
            title = title,
            isLoading = isLoading,
            isError = isError,
        )
    }

    override fun onUpdate(title: String) {
        store.accept(ServiceCategoryCreateStore.Intent.Update(title))
    }

    override fun onCreate(title: String) {
        store.accept(ServiceCategoryCreateStore.Intent.Create(title))
    }

    @JvmInline
    value class CreatedCategory(
        val id: Long,
    )

}