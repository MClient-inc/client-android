package ru.mclient.mvi.home

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.analytics.AnalyticsNetworkSource
import ru.mclient.network.analytics.GetCompanyAnalyticsInput
import java.time.LocalDate

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class HomeAnalyticsStoreImpl(
    storeFactory: StoreFactory,
    params: HomeAnalyticsStore.Params,
    analyticsNetworkSource: AnalyticsNetworkSource,
    coroutineDispatcher: CoroutineDispatcher,
) : HomeAnalyticsStore,
    Store<HomeAnalyticsStore.Intent, HomeAnalyticsStore.State, HomeAnalyticsStore.Label> by storeFactory.create(
        name = "HomeAnalyticsStoreImpl",
        initialState = HomeAnalyticsStore.State(
            analytics = null,
            isLoading = true,
            isFailure = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, analyticsNetworkSource, coroutineDispatcher) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    analytics = message.let {
                        HomeAnalyticsStore.State.Analytics(
                            totalSum = it.totalSum,
                            averageSum = it.averageSum,
                            comeCount = it.comeCount,
                            notComeCount = it.notComeCount,
                            waitingCome = it.waitingCount,
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
        private val params: HomeAnalyticsStore.Params,
        private val analyticsSource: AnalyticsNetworkSource,
        coroutineDispatcher: CoroutineDispatcher,
    ) :
        SyncCoroutineExecutor<HomeAnalyticsStore.Intent, Action, HomeAnalyticsStore.State, Message, HomeAnalyticsStore.Label>(
            coroutineDispatcher
        ) {

        override fun executeAction(
            action: Action,
            getState: () -> HomeAnalyticsStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId)
            }
        }

        override fun executeIntent(
            intent: HomeAnalyticsStore.Intent,
            getState: () -> HomeAnalyticsStore.State,
        ) {
            when (intent) {
                is HomeAnalyticsStore.Intent.Refresh ->
                    loadCompanies(params.companyId)
            }
        }

        private fun loadCompanies(
            companyId: Long,
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val recordsResponse = analyticsSource.getCompanyAnalytics(
                        GetCompanyAnalyticsInput(companyId, LocalDate.now(), LocalDate.now())
                    )
                    syncDispatch(
                        Message.Loaded(
                            totalSum = recordsResponse.totalSum,
                            averageSum = recordsResponse.averageSum,
                            comeCount = recordsResponse.comeCount,
                            notComeCount = recordsResponse.notComeCount,
                            waitingCount = recordsResponse.waitingCount,
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
            val totalSum: Long,
            val averageSum: Long,
            val comeCount: Int,
            val notComeCount: Int,
            val waitingCount: Int,
        ) : Message()

    }
}