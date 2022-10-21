package ru.mclient.mvi.service.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.service.GetServicesForCategoryAndCompanyInput
import ru.mclient.network.service.ServiceNetworkSource
import ru.mclient.network.servicecategory.GetServiceCategoryByIdInput
import ru.mclient.network.servicecategory.ServiceCategoryNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ServiceListForCategoryAndCompanyStoreImpl(
    storeFactory: StoreFactory,
    params: ServiceListForCategoryAndCompanyStore.Params,
    serviceSource: ServiceNetworkSource,
    categoriesSource: ServiceCategoryNetworkSource,
) : ServiceListForCategoryAndCompanyStore,
    Store<ServiceListForCategoryAndCompanyStore.Intent, ServiceListForCategoryAndCompanyStore.State, ServiceListForCategoryAndCompanyStore.Label> by storeFactory.create(
        name = "ServiceListForCategoryAndCompanyStoreImpl",
        initialState = ServiceListForCategoryAndCompanyStore.State(
            services = emptyList(),
            category = null,
            isLoading = true,
            isFailure = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, serviceSource, categoriesSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    services = message.services.map { company ->
                        ServiceListForCategoryAndCompanyStore.State.Service(
                            id = company.id,
                            title = company.title,
                        )
                    },
                    category = ServiceListForCategoryAndCompanyStore.State.ServiceCategory(
                        id = message.category.id,
                        title = message.category.title
                    ),
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
        private val params: ServiceListForCategoryAndCompanyStore.Params,
        private val serviceSource: ServiceNetworkSource,
        private val categoriesSource: ServiceCategoryNetworkSource,
    ) :
        SyncCoroutineExecutor<ServiceListForCategoryAndCompanyStore.Intent, Action, ServiceListForCategoryAndCompanyStore.State, Message, ServiceListForCategoryAndCompanyStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> ServiceListForCategoryAndCompanyStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId, params.categoryId)
            }
        }

        override fun executeIntent(
            intent: ServiceListForCategoryAndCompanyStore.Intent,
            getState: () -> ServiceListForCategoryAndCompanyStore.State
        ) {
            when (intent) {
                is ServiceListForCategoryAndCompanyStore.Intent.Refresh ->
                    loadCompanies(params.companyId, params.categoryId)
            }
        }

        private fun loadCompanies(
            companyId: Long,
            categoryId: Long,
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val categoryResponse = categoriesSource.getServiceCategoryById(
                        GetServiceCategoryByIdInput(categoryId)
                    )
                    val servicesResponse = serviceSource.getServicesForCategoryAndCompany(
                        GetServicesForCategoryAndCompanyInput(companyId, categoryId)
                    )
                    syncDispatch(
                        Message.Loaded(
                            servicesResponse.services.map { service ->
                                Message.Loaded.Service(
                                    id = service.id,
                                    title = service.title,
                                )
                            },
                            category = Message.Loaded.ServiceCategory(
                                id = categoryResponse.id,
                                title = categoryResponse.title,
                            )
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
                }
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
            val services: List<Service>,
            val category: ServiceCategory,
        ) : Message() {
            class Service(
                val id: Long,
                val title: String,
            )

            class ServiceCategory(
                val id: Long,
                val title: String,
            )
        }

    }
}