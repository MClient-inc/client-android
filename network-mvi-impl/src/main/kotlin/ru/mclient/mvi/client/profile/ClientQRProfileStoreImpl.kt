package ru.mclient.mvi.client.profile

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.network.client.ClientNetworkSource
import ru.mclient.network.client.GetClientCardInput

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class ClientQRProfileStoreImpl(
    storeFactory: StoreFactory,
    params: ClientQRProfileStore.Params,
    state: ClientQRProfileStore.State?,
    clientSource: ClientNetworkSource,
) :
    ClientQRProfileStore,
    Store<ClientQRProfileStore.Intent, ClientQRProfileStore.State, ClientQRProfileStore.Label> by storeFactory.create(
        name = "ClientQRProfileStoreImpl",
        initialState = state ?: ClientQRProfileStore.State(
            code = null,
            isLoading = false,
            isVisible = false,
            isFailure = false,
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { Executor(params, clientSource) },
        reducer = { it }
    ) {

    class Executor(
        private val params: ClientQRProfileStore.Params,
        private val clientSource: ClientNetworkSource,
    ) :
        SyncCoroutineExecutor<ClientQRProfileStore.Intent, Action, ClientQRProfileStore.State, ClientQRProfileStore.State, ClientQRProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ClientQRProfileStore.State) {
            when (action) {
                Action.FirstLoad -> loadCode(params.clientId, getState)
            }
        }

        override fun executeIntent(
            intent: ClientQRProfileStore.Intent,
            getState: () -> ClientQRProfileStore.State,
        ) {
            when (intent) {
                is ClientQRProfileStore.Intent.Move -> dispatch(getState().copy(isVisible = intent.isVisible))
            }
        }

        private fun loadCode(
            clientId: Long,
            getState: () -> ClientQRProfileStore.State,
        ) {
            val state = getState()
            dispatch(state.copy(isLoading = true))
            scope.launch {
                try {
                    val response = clientSource.getClientCard(GetClientCardInput(clientId))
                    syncDispatch(
                        state.copy(isFailure = false, isLoading = false, code = response.code)
                    )
                } catch (e: Exception) {
                    syncDispatch(state.copy(isFailure = true, isLoading = false))
                }
            }
        }

    }


    sealed class Action {
        object FirstLoad : Action()
    }

}