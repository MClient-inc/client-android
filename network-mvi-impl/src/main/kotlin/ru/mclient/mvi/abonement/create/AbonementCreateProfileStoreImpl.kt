package ru.mclient.mvi.abonement.create

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam


@Factory
class AbonementCreateProfileStoreImpl(
    storeFactory: StoreFactory,
    @InjectedParam
    state: AbonementCreateProfileStore.State?,
) : AbonementCreateProfileStore,
    Store<AbonementCreateProfileStore.Intent, AbonementCreateProfileStore.State, AbonementCreateProfileStore.Label> by storeFactory.create(
        name = "AbonementCreateProfileStoreImpl",
        initialState = state ?: AbonementCreateProfileStore.State(
            title = "",
            isSuccess = false,
        ),
        executorFactory = { StoreExecutor() },
        reducer = { it },
    ) {

    class StoreExecutor :
        CoroutineExecutor<AbonementCreateProfileStore.Intent, Nothing, AbonementCreateProfileStore.State, AbonementCreateProfileStore.State, AbonementCreateProfileStore.Label>() {

        override fun executeIntent(
            intent: AbonementCreateProfileStore.Intent,
            getState: () -> AbonementCreateProfileStore.State,
        ) {
            when (intent) {
                is AbonementCreateProfileStore.Intent.Update -> {
                    dispatch(
                        AbonementCreateProfileStore.State(
                            intent.title.length >= 2,
                            intent.title
                        )
                    )
                }
            }
        }
    }

}