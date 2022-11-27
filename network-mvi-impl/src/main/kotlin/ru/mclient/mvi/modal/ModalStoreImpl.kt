package ru.mclient.mvi.modal

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.koin.core.annotation.Factory

@Factory
class ModalStoreImpl(
    storeFactory: StoreFactory,
    state: ModalStore.State?,
    params: ModalStore.Params?,
) : ModalStore,
    Store<ModalStore.Intent, ModalStore.State, ModalStore.Label> by storeFactory.create(
        name = "ModalStoreImpl",
        initialState = state ?: ModalStore.State(params?.isVisible ?: false),
        executorFactory = { Executor() },
        reducer = { it }
    ) {

    class Executor :
        CoroutineExecutor<ModalStore.Intent, Nothing, ModalStore.State, ModalStore.State, ModalStore.Label>() {

        override fun executeIntent(
            intent: ModalStore.Intent,
            getState: () -> ModalStore.State,
        ) {
            dispatch(ModalStore.State(intent.isVisible))
            publish(ModalStore.Label(intent.isVisible))
        }

    }
}