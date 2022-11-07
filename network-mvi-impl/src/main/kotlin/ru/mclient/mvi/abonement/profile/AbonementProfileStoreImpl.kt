package ru.mclient.mvi.abonement.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.abonement.AbonementNetworkSource
import ru.mclient.network.abonement.GetAbonementByIdInput
import java.time.LocalDateTime


@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class AbonementProfileStoreImpl(
    storeFactory: StoreFactory,
    params: AbonementProfileStore.Params,
    staffSource: AbonementNetworkSource,
) : AbonementProfileStore,
    Store<AbonementProfileStore.Intent, AbonementProfileStore.State, AbonementProfileStore.Label> by storeFactory.create(
        name = "AbonnementProfileStoreImpl",
        initialState = AbonementProfileStore.State(
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
                    abonement = AbonementProfileStore.State.Abonement(
                        id = message.abonement.id,
                        title = message.abonement.title,
                        subabonements = message.abonement.subabonements.map {
                            AbonementProfileStore.State.Subabonement(
                                id = it.id,
                                title = it.title,
                                liveTimeInMillis = it.liveTimeInMillis,
                                usages = it.usages,
                                availableUntil = it.availableUntil,
                            )
                        },
                        services = message.abonement.services.map {
                            AbonementProfileStore.State.Service(
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
                    isRefreshing = abonement != null,
                )
            }
        }
    ) {

    class Executor(
        private val params: AbonementProfileStore.Params,
        private val abonnementSource: AbonementNetworkSource,
    ) :
        SyncCoroutineExecutor<AbonementProfileStore.Intent, Action, AbonementProfileStore.State, Message, AbonementProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> AbonementProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadAbonement(params.abonementId)
            }
        }

        override fun executeIntent(
            intent: AbonementProfileStore.Intent,
            getState: () -> AbonementProfileStore.State,
        ) {
            when (intent) {
                AbonementProfileStore.Intent.Refresh -> loadAbonement(params.abonementId)
            }
        }

        private fun loadAbonement(abonementId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
                    val response =
                        abonnementSource.getAbonementById(GetAbonementByIdInput(abonementId))
                    dispatch(
                        Message.Loaded(
                            abonement = Message.Loaded.Abonement(
                                id = response.abonement.id,
                                services = response.abonement.services.map {
                                    Message.Loaded.Service(
                                        id = it.id,
                                        title = it.title,
                                        cost = it.cost
                                    )
                                },
                                subabonements = response.abonement.subabonements.map {
                                    Message.Loaded.Subabonement(
                                        id = it.id,
                                        liveTimeInMillis = it.liveTimeInMillis,
                                        availableUntil = it.availableUntil,
                                        usages = it.usages,
                                        title = it.title
                                    )
                                },
                                title = response.abonement.title,
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
            val abonement: Abonement,
        ) : Message() {
            data class Abonement(
                val id: Long,
                val title: String,
                val subabonements: List<Subabonement>,
                val services: List<Service>,
            )

            data class Subabonement(
                val id: Long,
                val title: String,
                val usages: Int,
                val liveTimeInMillis: Long,
                val availableUntil: LocalDateTime,
            )

            data class Service(
                val id: Long,
                val title: String,
                val cost: Long,
            )
        }

    }
}