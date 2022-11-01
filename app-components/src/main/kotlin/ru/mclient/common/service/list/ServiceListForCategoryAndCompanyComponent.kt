package ru.mclient.common.service.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.service.list.ServiceListForCategoryAndCompanyStore

class ServiceListForCategoryAndCompanyComponent(
    componentContext: DIComponentContext,
    categoryId: Long,
    companyId: Long,
    private val onCategoryTitle: (String?) -> Unit,
    private val onCreate: () -> Unit,
    private val onSelect: (Long) -> Unit,
) : ServiceList, DIComponentContext by componentContext {

    private val store: ServiceListForCategoryAndCompanyStore =
        getParameterizedStore {
            ServiceListForCategoryAndCompanyStore.Params(
                categoryId = categoryId,
                companyId = companyId,
            )
        }

    override val state: ServiceListState by store.states(this) { it.toState() }

    private fun ServiceListForCategoryAndCompanyStore.State.toState(): ServiceListState {
        val category = category
        when {
            isFailure && category == null -> onCategoryTitle.invoke("Услуги категории")
            category != null -> onCategoryTitle.invoke(category.title)
            else -> onCategoryTitle.invoke(null)
        }
        return ServiceListState(
            services = services.map { it.toState() },
            isLoading = isLoading,
            isRefreshing = isLoading && services.isNotEmpty()
        )
    }

    private fun ServiceListForCategoryAndCompanyStore.State.Service.toState(): ServiceListState.Service {
        return ServiceListState.Service(id = id, title = title)
    }

    override fun onRefresh() {
        store.accept(ServiceListForCategoryAndCompanyStore.Intent.Refresh)
    }

    override fun onService(serviceId: Long) {
        onSelect.invoke(serviceId)
    }

    override fun onCreate() {
        onCreate.invoke()
    }
}