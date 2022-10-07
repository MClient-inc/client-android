package ru.mclient.common.utils

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.mclient.common.DIComponentContext

internal inline fun <reified T : Store<*, *, *>> DIComponentContext.getStore(): T {
    return instanceKeeper.getStore(this::get)
}

internal inline fun <reified SavedState : Parcelable, reified State : Any, reified T : Store<*, State, *>> DIComponentContext.getStoreSavedState(
    key: String,
    crossinline save: (State) -> SavedState,
    crossinline restore: (SavedState) -> State,
): T {
    val store = instanceKeeper.getStore {
        get<T>(parameters = {
            val value = stateKeeper.consume<SavedState>(key)
            parametersOf(value?.let { restore(it) })
        })
    }
    stateKeeper.register(key) { save(store.state) }
    return store
}


internal inline fun <reified State : Parcelable, reified T : Store<*, State, *>> DIComponentContext.getStoreSavedState(key: String, ): T {
    return getStoreSavedState(
        key,
        save = { it },
        restore = { it }
    )
}

//private operator fun ParametersHolder?.plus(parameters: ParametersHolder?): ParametersHolder {
//    if (this == null && parameters == null) return parametersOf()
//    if (this != null && parameters == null) return this
//    if (parameters != null && this == null) return parameters
//    return ParametersHolder((this!!.values + parameters!!.values).toMutableList())
//}