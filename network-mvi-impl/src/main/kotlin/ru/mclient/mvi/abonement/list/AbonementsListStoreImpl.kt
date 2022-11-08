package ru.mclient.mvi.abonement.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.GetAbonementsForCompanyInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class AbonementsListStoreImpl(
    storeFactory: StoreFactory,
    params: AbonementsListStore.Params,
    abonementSource: AbonementNetworkSource,
) : AbonementsListStore,
    Store<AbonementsListStore.Intent, AbonementsListStore.State, AbonementsListStore.Label> by storeFactory.create(
        name = "AbonementsListStoreImpl",
        initialState = AbonementsListStore.State(
            abonements = emptyList(),
            isLoading = true,
            isFailure = false,
            isRefreshing = false,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, abonementSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    abonements = message.abonements.map { abonement ->
                        AbonementsListStore.State.Abonement(
                            id = abonement.id,
                            title = abonement.title,
                            subabonements = abonement.subabonements.map {
                                AbonementsListStore.State.Subabonement(it.id, it.title, it.cost)
                            }
                        )
                    },
                    isLoading = false,
                    isFailure = false,
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                    isRefreshing = abonements.isNotEmpty()
                )
            }
        }
    ) {

    class Executor(
        private val params: AbonementsListStore.Params,
        private val abonementSource: AbonementNetworkSource,
    ) :
        SyncCoroutineExecutor<AbonementsListStore.Intent, Action, AbonementsListStore.State, Message, AbonementsListStore.Label>() {

        override fun executeAction(
            action: Action,
            getState: () -> AbonementsListStore.State,
        ) {
            when (action) {
                is Action.FirstLoad ->
                    loadCompanies(params.companyId)
            }
        }

        override fun executeIntent(
            intent: AbonementsListStore.Intent,
            getState: () -> AbonementsListStore.State,
        ) {
            when (intent) {
                is AbonementsListStore.Intent.Refresh ->
                    loadCompanies(params.companyId)
            }
        }

        private fun loadCompanies(
            companyId: Long,
        ) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response = abonementSource.getAbonementsForCompany(
                        GetAbonementsForCompanyInput(companyId)
                    )
                    syncDispatch(Message.Loaded(
                        response.abonements.map { abonement ->
                            Message.Loaded.Abonement(
                                id = abonement.id,
                                title = abonement.title,
                                subabonements = abonement.subabonements.map {
                                    Message.Loaded.Subabonement(
                                        id = it.id,
                                        title = it.title,
                                        cost = it.cost,
                                    )
                                }
                            )
                        }
                    ))
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
            val abonements: List<Abonement>,
        ) : Message() {

            data class Abonement(
                val id: Long,
                val title: String,
                val subabonements: List<Subabonement>,
            )

            data class Subabonement(
                val id: Long,
                val title: String,
                val cost: Long,
            )

        }
    }

}