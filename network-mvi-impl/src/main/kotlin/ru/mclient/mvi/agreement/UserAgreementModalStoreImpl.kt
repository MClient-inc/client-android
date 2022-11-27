package ru.mclient.mvi.agreement

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.mclient.mvi.SyncCoroutineExecutor
import ru.mclient.mvi.agreement.AgreementStore.Param.AgreementType.*
import ru.mclient.network.agreement.AgreementNetworkSource

@OptIn(ExperimentalMviKotlinApi::class)
@Factory
class AgreementStoreImpl(
    storeFactory: StoreFactory,
    agreementNetworkSource: AgreementNetworkSource,
    param: AgreementStore.Param?,
    dispatcher: CoroutineDispatcher,
) : AgreementStore,
    Store<AgreementStore.Intent, AgreementStore.State, AgreementStore.Label> by storeFactory.create(
        name = "UserAgreementStoreImpl",
        bootstrapper = coroutineBootstrapper {
            if (param?.loadOnStart == true) {
                dispatch(AgreementStore.Intent.Load)
            }
        },
        initialState = AgreementStore.State(
            title = null,
            content = null,
            isLoaded = false,
            isError = false,
        ),
        executorFactory = {
            Executor(
                agreementNetworkSource,
                param?.type ?: USER_AGREEMENT,
                dispatcher
            )
        },
        reducer = { message -> message }
    ) {

    class Executor(
        private val agreementNetworkSource: AgreementNetworkSource,
        private val type: AgreementStore.Param.AgreementType,
        dispatcher: CoroutineDispatcher,
    ) : SyncCoroutineExecutor<AgreementStore.Intent, AgreementStore.Intent, AgreementStore.State, AgreementStore.State, Nothing>(
        dispatcher
    ) {

        override fun executeAction(
            action: AgreementStore.Intent,
            getState: () -> AgreementStore.State,
        ) {
            executeIntent(action, getState)
        }

        override fun executeIntent(
            intent: AgreementStore.Intent,
            getState: () -> AgreementStore.State,
        ) {
            when (intent) {
                is AgreementStore.Intent.Load -> {
                    val state = getState()
                    if (state.isLoaded) return
                    scope.launch {
                        try {
                            val response = when (type) {
                                USER_AGREEMENT ->
                                    agreementNetworkSource.getUserAgreement()

                                CLIENT_DATA_PROCESSING_AGREEMENT ->
                                    agreementNetworkSource.getClientDataProcessingAgreement()
                            }
                            syncDispatch(
                                AgreementStore.State(
                                    title = response.title,
                                    content = response.content,
                                    isLoaded = true,
                                    isError = false,
                                )
                            )
                        } catch (e: Exception) {
                            syncDispatch(
                                state.copy(isError = true)
                            )
                        }
                    }
                }
            }
        }

    }

}
