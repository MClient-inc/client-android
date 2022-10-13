package ru.mclient.mvi.company.profile

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.company.CompanyNetworkSource
import ru.mclient.network.company.GetCompanyInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class CompanyProfileStoreImpl(
    storeFactory: StoreFactory,
    params: CompanyProfileStore.Params,
    companiesSource: CompanyNetworkSource,
) : CompanyProfileStore,
    Store<CompanyProfileStore.Intent, CompanyProfileStore.State, CompanyProfileStore.Label> by storeFactory.create(
        name = "CompanyProfileStoreImpl",
        initialState = CompanyProfileStore.State(
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
                    CompanyProfileStore.State.Company(
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
        private val params: CompanyProfileStore.Params,
        private val companiesSource: CompanyNetworkSource,
    ) :
        SyncCoroutineExecutor<CompanyProfileStore.Intent, Action, CompanyProfileStore.State, Message, CompanyProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> CompanyProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadCompany(params.companyId)
            }
        }

        override fun executeIntent(
            intent: CompanyProfileStore.Intent,
            getState: () -> CompanyProfileStore.State
        ) {
            when (intent) {
                CompanyProfileStore.Intent.Refresh -> loadCompany(params.companyId)
            }
        }

        private fun loadCompany(companyId: Long) {
            Log.d("AcceptTester", "load")
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val company = companiesSource.getCompany(GetCompanyInput(companyId))
                    syncDispatch(
                        Message.Loaded(
                            Message.Loaded.Company(
                                id = company.company.id,
                                title = company.company.title,
                                codename = company.company.codename,
                                description = company.company.description,
                                icon = company.company.icon,
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