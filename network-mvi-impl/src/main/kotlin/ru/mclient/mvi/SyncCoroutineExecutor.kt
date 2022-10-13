package ru.mclient.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class SyncCoroutineExecutor<in Intent : Any, in Action : Any, in State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext = Dispatchers.Main
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext) {

    suspend fun syncDispatch(message: Message) {
        withContext(Dispatchers.Main) {
            dispatch(message)
        }
    }

    suspend fun syncPublish(label: Label) {
        withContext(Dispatchers.Main) {
            publish(label)
        }
    }

}