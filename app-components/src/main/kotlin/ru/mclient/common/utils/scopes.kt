package ru.mclient.common.utils

import androidx.annotation.CallSuper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

fun ComponentContext.createCoroutineScope(coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()): CoroutineScope {
    val scope = CoroutineScope(coroutineContext)
    lifecycle.doOnDestroy { scope.cancel() }
    return scope
}


abstract class CoroutineInstance : InstanceKeeper.Instance {

    protected val scope = MainScope()

    @CallSuper
    override fun onDestroy() {
        scope.cancel()
    }

}