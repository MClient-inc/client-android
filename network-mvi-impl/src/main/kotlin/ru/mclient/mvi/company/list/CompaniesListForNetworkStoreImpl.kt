package ru.mclient.mvi.company.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.company.CompanyNetworkSource
import ru.mclient.network.company.GetCompaniesByNetworkInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class CompaniesListForNetworkStoreImpl(
    storeFactory: StoreFactory,
    params: CompaniesListForNetworkStore.Params,
    companiesSource: CompanyNetworkSource,
) : CompaniesListForNetworkStore,
    Store<CompaniesListForNetworkStore.Intent, CompaniesListForNetworkStore.State, CompaniesListForNetworkStore.Label> by storeFactory.create(
        name = "CompaniesListForNetworkStoreImpl",
        initialState = CompaniesListForNetworkStore.State(
            emptyList(),
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, companiesSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    message.companies.map { company ->
                        CompaniesListForNetworkStore.State.Company(
                            company.id,
                            company.title,
                            company.codename,
                            company.icon,
                        )
                    },
                    isFailure = false,
                    isLoading = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: CompaniesListForNetworkStore.Params,
        private val companiesSource: CompanyNetworkSource,
    ) :
        SyncCoroutineExecutor<CompaniesListForNetworkStore.Intent, Action, CompaniesListForNetworkStore.State, Message, CompaniesListForNetworkStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> CompaniesListForNetworkStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.networkId)
            }
        }

        override fun executeIntent(
            intent: CompaniesListForNetworkStore.Intent,
            getState: () -> CompaniesListForNetworkStore.State
        ) {
            when (intent) {
                is CompaniesListForNetworkStore.Intent.Refresh ->
                    loadCompanies(params.networkId)
            }
        }

        private fun loadCompanies(
            networkId: Long
        ) {
            dispatch(Message.Loading)
            scope.launch {
                val response = try {
                    companiesSource.getCompanies(GetCompaniesByNetworkInput(networkId.toString()))
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
                }
                syncDispatch(
                    Message.Loaded(
                        response.companies.map { company ->
                            Message.Loaded.Company(
                                id = company.id.toLong(),
                                title = company.title,
                                codename = company.codename,
                                icon = company.icon,
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
            val companies: List<Company>,
        ) : Message() {
            class Company(
                val id: Long,
                val title: String,
                val codename: String,
                val icon: String?,
            )
        }

    }
}