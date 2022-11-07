package ru.mclient.mvi.abonnement.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonnement.AbonnementNetworkSource
import ru.mclient.network.abonnement.GetAbonnementByIdInput
import java.time.LocalDateTime


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
                        title = message.abonnement.title,
                        subAbonnements = message.abonnement.subAbonnements.map {
                            AbonnementProfileStore.State.SubAbonnement(
                                id = it.id,
                                title = it.title,
                                liveTimeInMillis = it.liveTimeInMillis,
                                usages = it.usages,
                                availableUntil = it.availableUntil,
                            )
                        },
//                        services = message.abonnement.services.map {
//                            AbonnementProfileStore.State.Service(
//                                id = it.id,
//                                cost = it.cost,
//                                title = it.title
//                            )
//                        }
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
                    val response =
                        abonnementSource.getAbonnementById(GetAbonnementByIdInput(abonnementId))
                    dispatch(
                        Message.Loaded(
                            abonnement = Message.Loaded.Abonnement(
                                id = response.abonnement.id,
//                                services = response,
                                subAbonnements = response.abonnement.subabonements.map {
                                    Message.Loaded.SubAbonnement(
                                        id = it.id,
                                        liveTimeInMillis = it.liveTimeInMillis,
                                        availableUntil = it.availableUntil,
                                        usages = it.usages,
                                        title = it.title
                                    )
                                },
                                title = response.abonnement.title,
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
            val abonnement: Abonnement,
        ) : Message() {
            data class Abonnement(
                val id: Long,
                val title: String,
                val subAbonnements: List<SubAbonnement>,
//                val services: List<Service>,
            )

            data class SubAbonnement(
                val id: Long,
                val title: String,
                val usages: Int,
                val liveTimeInMillis: Long,
                val availableUntil: LocalDateTime
            )

            data class Service(
                val id: Long,
                val title: String,
                val cost: Long
            )
        }

    }
}