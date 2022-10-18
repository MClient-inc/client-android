package ru.mclient.mvi.companynetwork.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.company.CompanyNetworkSource
import ru.mclient.network.company.GetNetworkInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class CompanyNetworkProfileByIdStoreImpl(
    storeFactory: StoreFactory,
    params: CompanyNetworkProfileStore.Params,
    companiesSource: CompanyNetworkSource,
) : CompanyNetworkProfileStore,
    Store<CompanyNetworkProfileStore.Intent, CompanyNetworkProfileStore.State, CompanyNetworkProfileStore.Label> by storeFactory.create(
        name = "CompanyNetworkProfileByIdStoreImpl",
        initialState = CompanyNetworkProfileStore.State(
            null,
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
                    CompanyNetworkProfileStore.State.CompanyNetwork(
                        id = message.company.id,
                        title = message.company.title,
                        codename = message.company.codename,
                        description = message.company.description,
                        icon = message.company.icon,
                    ),
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
        private val params: CompanyNetworkProfileStore.Params,
        private val companiesSource: CompanyNetworkSource,
    ) :
        SyncCoroutineExecutor<CompanyNetworkProfileStore.Intent, Action, CompanyNetworkProfileStore.State, Message, CompanyNetworkProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> CompanyNetworkProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadCompany(params.networkId)
            }
        }

        override fun executeIntent(
            intent: CompanyNetworkProfileStore.Intent,
            getState: () -> CompanyNetworkProfileStore.State
        ) {
            when (intent) {
                CompanyNetworkProfileStore.Intent.Refresh -> loadCompany(params.networkId)
            }
        }

        private fun loadCompany(networkId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val company = companiesSource.getNetwork(GetNetworkInput(networkId))
                    syncDispatch(
                        Message.Loaded(
                            Message.Loaded.Company(
                                id = company.network.id,
                                title = company.network.title,
                                codename = company.network.codename,
                                description = company.network.description,
                                icon = company.network.icon,
                            )
                        )
                    )
                } catch (e: Exception) {
                    syncDispatch(Message.Failed)
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
            val company: Company
        ) : Message() {
            class Company(
                val id: Long,
                val title: String,
                val codename: String,
                val description: String,
                val icon: String?,
            )
        }

    }
}