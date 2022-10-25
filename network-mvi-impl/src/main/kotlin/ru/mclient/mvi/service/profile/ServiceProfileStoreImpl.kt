package ru.mclient.mvi.service.profile

import android.app.Service
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.staff.profile.StaffProfileStore
import ru.mclient.network.service.ServiceNetworkSource

@Factory
class ServiceProfileStoreImpl(
    storeFactory: StoreFactory,
    params: StaffProfileStore.Params,
    serviceSource: ServiceNetworkSource
) : ServiceProfileStore,
    Store<ServiceProfileStore.Intent, ServiceProfileStore.State, ServiceProfileStore.Label> by storeFactory.create(
        name = "ServiceProfileStoreImpl",
        initialState = ServiceProfileStore.State(
            null,
            isFailure = false,
            isLoading = true
        ),
        bootstrapper = coroutineBootstrapper {
            dispatch(Action.FirstLoad)
        },
        executorFactory = { TODO() },
        reducer = {TODO()}
    ) {

    class Executor(
        private val params: ServiceProfileStore.Params,
        private val serviceSource: ServiceNetworkSource
    ) : SyncCoroutineExecutor<ServiceProfileStore.Intent, Action, ServiceProfileStore.State, Message, ServiceProfileStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ServiceProfileStore.State) {
            when (action) {
                Action.FirstLoad -> TODO()
            }
        }

        private fun loadService(serviceId: Long) {
            dispatch(Message.Loading)
            scope.launch {
                try {
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
            val service: Service
        ) : Message() {
            class Service(
                val id: Long,
                val title: String,
                val description: String,
                val cost: String,
            )
        }

    }
}