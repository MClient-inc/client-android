package ru.mclient.mvi.companynetwork.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.company.CompanyNetworkSource
import ru.mclient.network.company.GetCompanyNetworksInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class CompanyNetworksListForAccountStoreImpl(
    storeFactory: StoreFactory,
    params: CompanyNetworksListForAccountStore.Params,
    companiesSource: CompanyNetworkSource,
) : CompanyNetworksListForAccountStore,
    Store<CompanyNetworksListForAccountStore.Intent, CompanyNetworksListForAccountStore.State, CompanyNetworksListForAccountStore.Label> by storeFactory.create(
        name = "CompanyNetworksListForAccountStoreImpl",
        initialState = CompanyNetworksListForAccountStore.State(
            networks = emptyList(),
            accountId = params.accountId,
            isLoading = true,
            isFailure = true
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
                    message.networks.map { company ->
                        CompanyNetworksListForAccountStore.State.CompanyNetwork(
                            company.id,
                            company.title,
                            company.codename,
                            company.icon,
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
        private val params: CompanyNetworksListForAccountStore.Params,
        private val companiesSource: CompanyNetworkSource,
    ) :
        SyncCoroutineExecutor<CompanyNetworksListForAccountStore.Intent, Action, CompanyNetworksListForAccountStore.State, Message, CompanyNetworksListForAccountStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> CompanyNetworksListForAccountStore.State
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.accountId)
            }
        }

        override fun executeIntent(
            intent: CompanyNetworksListForAccountStore.Intent,
            getState: () -> CompanyNetworksListForAccountStore.State
        ) {
            when (intent) {
                is CompanyNetworksListForAccountStore.Intent.Refresh ->
                    loadCompanies(params.accountId)
            }
        }

        private fun loadCompanies(
            accountId: Long
        ) {
            dispatch(Message.Loading)
            scope.launch {
                val response = try {
                    companiesSource.getNetworks(GetCompanyNetworksInput(accountId))
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
                    return@launch
                }
                syncDispatch(
                    Message.Loaded(
                        response.networks.map { network ->
                            Message.Loaded.CompanyNetwork(
                                id = network.id,
                                title = network.title,
                                codename = network.codename,
                                icon = network.icon,
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
            val networks: List<CompanyNetwork>,
        ) : Message() {
            class CompanyNetwork(
                val id: Long,
                val title: String,
                val codename: String,
                val icon: String?,
            )
        }

    }
}