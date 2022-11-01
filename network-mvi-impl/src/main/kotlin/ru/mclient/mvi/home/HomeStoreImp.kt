package ru.mclient.mvi.home

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.company.CompanyNetworkSource
import ru.mclient.network.company.GetCompanyInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class HomeStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    params: HomeStore.Params,
    @InjectedParam
    state: HomeStore.State?,
    companyNetworkSource: CompanyNetworkSource,
) : HomeStore,
    Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
        name = "HomeStoreImpl",
        initialState = state ?: HomeStore.State(
            company = null,
            isLoading = false,
            isFailure = false,
            isFirstLoading = true,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, companyNetworkSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    company = HomeStore.State.Company(message.company.id, message.company.title),
                    isLoading = false,
                    isFailure = false,
                    isFirstLoading = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                )
            }
        }
    ) {

    class Executor(
        private val params: HomeStore.Params,
        private val companyNetworkSource: CompanyNetworkSource,
    ) :
        SyncCoroutineExecutor<HomeStore.Intent, Action, HomeStore.State, Message, HomeStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> HomeStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId, false)
            }
        }

        override fun executeIntent(
            intent: HomeStore.Intent,
            getState: () -> HomeStore.State,
        ) {
            when (intent) {
                is HomeStore.Intent.Refresh ->
                    loadCompanies(params.companyId, getState().isLoading)
            }
        }

        private fun loadCompanies(
            companyId: Long,
            isLoading: Boolean,
        ) {
            if (isLoading) {
                return
            }
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val company = companyNetworkSource.getCompany(GetCompanyInput(companyId))
                    syncDispatch(
                        Message.Loaded(
                            company = Message.Loaded.Company(
                                id = company.company.id,
                                title = company.company.title,
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
            val company: Company,
        ) : Message() {

            data class Company(
                val id: Long,
                val title: String,
            )
        }

    }
}