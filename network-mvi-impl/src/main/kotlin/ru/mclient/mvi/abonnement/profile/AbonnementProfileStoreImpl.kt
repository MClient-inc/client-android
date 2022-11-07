package ru.mclient.mvi.abonnement.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonnement.AbonnementNetworkSource


@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class AbonnementProfileStoreImpl(
    storeFactory: StoreFactory,
    params: AbonnementProfileStore.Params,
    staffSource: AbonnementNetworkSource,
) : AbonnementProfileStore,
    Store<AbonnementProfileStore.Intent, AbonnementProfileStore.State, AbonnementProfileStore.Label> by storeFactory.create(
        name = "AbonnementProfileStoreImpl",
        initialState = AbonnementProfileStore.State(
            null,
            isFailure = false,
            isLoading = true,
            isRefreshing = false,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, staffSource) },
        reducer = { message ->
            when (message) {
                Message.Failed -> copy(
                    isFailure = true,
                    isLoading = false,
                )

                is Message.Loaded -> copy(
                    abonnement = AbonnementProfileStore.State.Abonnement(
                        id = message.abonnement.id,
                        name = message.abonnement.name,
                        subAbonnements = message.abonnement.subAbonnements.map {
                            AbonnementProfileStore.State.SubAbonnement(
                                id = it.id,
                                name = it.name,
                                maxTimesNumberToUse = it.maxTimesNumberToUse
                            )
                        },
                        services = message.abonnement.services.map {
                            AbonnementProfileStore.State.Service(
                                id = it.id,
                                cost = it.cost,
                                title = it.title
                            )
                        }
                    ),
                    isFailure = false,
                    isLoading = false,
                    isRefreshing = false
                )

                is Message.Loading -> copy(
                    isFailure = false,
                    isLoading = true,
                    isRefreshing = abonnement != null,
                )
            }
        }
    ) {

    class Executor(
        private val params: AbonnementProfileStore.Params,
        private val abonnementSource: AbonnementNetworkSource,
    ) :
        SyncCoroutineExecutor<AbonnementProfileStore.Intent, Action, AbonnementProfileStore.State, Message, AbonnementProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> AbonnementProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadAbonnement(params.abonnementId)
            }
        }

        override fun executeIntent(
            intent: AbonnementProfileStore.Intent,
            getState: () -> AbonnementProfileStore.State,
        ) {
            when (intent) {
                AbonnementProfileStore.Intent.Refresh -> loadAbonnement(params.abonnementId)
            }
        }

        private fun loadAbonnement(abonnementId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
//                    val response = abonnementSource.getAbonnementById(GetAbonnementByIdInput(abonnementId))
//                    dispatch(
//                        Message.Loaded(
//                            abonnement = Message.Loaded.Abonnement(
//                                id =,
//                                services = ,
//                                subAbonnements = ,
//                                name = ,
//                            )
//                        )
//                    )
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
            val abonnement: Abonnement,
        ) : Message() {
            data class Abonnement(
                val id: Long,
                val name: String,
                val subAbonnements: List<SubAbonnement>,
                val services: List<Service>,
            )

            data class SubAbonnement(
                val id: Long,
                val name: String,
                val maxTimesNumberToUse: Int
            )

            data class Service(
                val id: Long,
                val title: String,
                val cost: Long
            )
        }

    }
}