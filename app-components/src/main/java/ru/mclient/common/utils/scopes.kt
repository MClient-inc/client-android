package ru.mclient.common.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun ComponentContext.createCoroutineScope(coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()): CoroutineScope {
    val scope = CoroutineScope(coroutineContext)
    lifecycle.doOnDestroy { scope.cancel() }
    return scope
}


abstract class CoroutineInstance : InstanceKeeper.Instance {

    protected val scope = MainScope()

    override fun onDestroy() {
        scope.cancel()
    }

}