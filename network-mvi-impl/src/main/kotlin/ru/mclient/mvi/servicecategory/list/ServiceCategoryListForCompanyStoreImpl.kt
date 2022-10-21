package ru.mclient.mvi.servicecategory.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.servicecategory.GetServiceCategoriesByCompanyInput
import ru.mclient.network.servicecategory.ServiceCategoryNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ServiceCategoryListForCompanyStoreImpl(
    storeFactory: StoreFactory,
    params: ServiceCategoriesListForCompanyStore.Params,
    categoriesSource: ServiceCategoryNetworkSource,
) : ServiceCategoriesListForCompanyStore,
    Store<ServiceCategoriesListForCompanyStore.Intent, ServiceCategoriesListForCompanyStore.State, ServiceCategoriesListForCompanyStore.Label> by storeFactory.create(
        name = "CompanyNetworksListForAccountStoreImpl",
        initialState = ServiceCategoriesListForCompanyStore.State(
            categories = emptyList(),
            companyId = params.companyId,
            isLoading = true,
            isFailure = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, categoriesSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    message.categories.map { company ->
                        ServiceCategoriesListForCompanyStore.State.ServiceCategory(
                            id = company.id,
                            title = company.title,
                        )
                    },
                    isLoading = false,
                    isFailure = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: ServiceCategoriesListForCompanyStore.Params,
        private val categoriesSource: ServiceCategoryNetworkSource,
    ) :
        SyncCoroutineExecutor<ServiceCategoriesListForCompanyStore.Intent, Action, ServiceCategoriesListForCompanyStore.State, Message, ServiceCategoriesListForCompanyStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> ServiceCategoriesListForCompanyStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId)
            }
        }

        override fun executeIntent(
            intent: ServiceCategoriesListForCompanyStore.Intent,
            getState: () -> ServiceCategoriesListForCompanyStore.State
        ) {
            when (intent) {
                is ServiceCategoriesListForCompanyStore.Intent.Refresh ->
                    loadCompanies(params.companyId)
            }
        }

        private fun loadCompanies(
            companyUd: Long
        ) {
            dispatch(Message.Loading)
            scope.launch {
                val response = try {
                    categoriesSource.getServiceCategoriesByCompany(
                        GetServiceCategoriesByCompanyInput(companyUd)
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
                }
                syncDispatch(
                    Message.Loaded(
                        response.categories.map { category ->
                            Message.Loaded.ServiceCategory(
                                id = category.id,
                                title = category.title,
                            )
                        }
                    )
                )
            }
        }

    }

    sealed class Action {
        object FirstLoad : Action()
    }

    sealed class Message {
        object Failed : Message()
        object Loading : Message()
        class Loaded(
            val categories: List<ServiceCategory>,
        ) : Message() {
            class ServiceCategory(
                val id: Long,
                val title: String,
            )
        }

    }
}