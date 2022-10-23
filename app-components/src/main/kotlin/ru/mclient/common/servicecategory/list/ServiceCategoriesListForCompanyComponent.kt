package ru.mclient.common.servicecategory.list

import androidx.compose.runtime.getValue
import ru.mclient.common.DIComponentContext
import ru.mclient.common.utils.getParameterizedStore
import ru.mclient.common.utils.states
import ru.mclient.mvi.servicecategory.list.ServiceCategoriesListForCompanyStore

class ServiceCategoriesListForCompanyComponent(
    componentContext: DIComponentContext,
    companyId: Long,
    private val onCategorySelected: (Long) -> Unit,
    private val onCreate: () -> Unit,
) : ServiceCategoriesList, DIComponentContext by componentContext {

    val store: ServiceCategoriesListForCompanyStore =
        getParameterizedStore { ServiceCategoriesListForCompanyStore.Params(companyId) }

    override val state: ServiceCategoriesListState by store.states(this) { it.toState() }

    fun ServiceCategoriesListForCompanyStore.State.toState(): ServiceCategoriesListState {
        return ServiceCategoriesListState(
            categories = categories.map { it.toState() },
            isLoading, isLoading && categories.isNotEmpty()
        )
    }

    fun ServiceCategoriesListForCompanyStore.State.ServiceCategory.toState(): ServiceCategoriesListState.ServiceCategory {
        return ServiceCategoriesListState.ServiceCategory(
            id = id,
            title = title
        )
    }

    override fun onRefresh() {
        store.accept(ServiceCategoriesListForCompanyStore.Intent.Refresh)
    }

    override fun onCategory(categoryId: Long) {
        onCategorySelected.invoke(categoryId)
    }

    override fun onCreate() {
        onCreate.invoke()
    }
}