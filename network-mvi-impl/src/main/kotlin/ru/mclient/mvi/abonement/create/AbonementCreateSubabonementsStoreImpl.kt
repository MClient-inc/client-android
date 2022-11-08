package ru.mclient.mvi.abonement.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import kotlin.random.Random

@Factory
class AbonementCreateSubabonementsStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: AbonementCreateSubabonementsStore.State?,
) : AbonementCreateSubabonementsStore,
    Store<AbonementCreateSubabonementsStore.Intent, AbonementCreateSubabonementsStore.State, AbonementCreateSubabonementsStore.Label> by storeFactory.create(
        name = "AbonementCreateSubabonementsStoreImpl",
        initialState = state ?: AbonementCreateSubabonementsStore.State(
            isSuccess = true,
            subabonements = listOf(),
            creation = AbonementCreateSubabonementsStore.State.Creation(
                title = "",
                usages = 0,
                cost = 0,
                isAvailable = true,
                isContinueAvailable = false,
            ),
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<AbonementCreateSubabonementsStore.Intent, Nothing, AbonementCreateSubabonementsStore.State, AbonementCreateSubabonementsStore.State, AbonementCreateSubabonementsStore.Label>() {

        override fun executeIntent(
            intent: AbonementCreateSubabonementsStore.Intent,
            getState: () -> AbonementCreateSubabonementsStore.State,
        ) {
            when (intent) {
                is AbonementCreateSubabonementsStore.Intent.Create -> {
                    val state = getState()
                    if (state.creation.title.length < 2 || state.creation.usages <= 0)
                        return
                    dispatch(
                        state.copy(
                            isSuccess = true,
                            subabonements = state.subabonements + AbonementCreateSubabonementsStore.State.Subabonement(
                                title = state.creation.title,
                                usages = state.creation.usages,
                                cost = state.creation.cost,
                                uniqueId = Random.nextInt(),
                            ),
                            creation = AbonementCreateSubabonementsStore.State.Creation(
                                title = "",
                                usages = 0,
                                cost = 0,
                                isAvailable = true,
                                isContinueAvailable = false,
                            )
                        )
                    )
                }

                is AbonementCreateSubabonementsStore.Intent.DeleteById -> {
                    val state = getState()
                    val sub = state.subabonements.filterNot { it.uniqueId == intent.id }
                    dispatch(
                        state.copy(
                            isSuccess = sub.isNotEmpty(),
                            subabonements = sub,
                        )
                    )
                }

                is AbonementCreateSubabonementsStore.Intent.Update -> {
                    val state = getState()
                    val usages = intent.usages.toIntOrNull() ?: 0
                    val cost = intent.cost.toLongOrNull() ?: 0
                    dispatch(
                        state.copy(
                            isSuccess = state.subabonements.isNotEmpty(),
                            creation = AbonementCreateSubabonementsStore.State.Creation(
                                title = intent.title,
                                usages = usages,
                                cost = cost,
                                isAvailable = true,
                                isContinueAvailable = intent.title.length >= 2 && usages > 0,
                            )
                        )
                    )
                }
            }
        }
    }

}