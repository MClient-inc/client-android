package ru.mclient.common.utils

import androidx.compose.runtime.mutableStateOf
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.observer
import org.koin.core.component.get
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import ru.mclient.common.DIComponentContext
import ru.mclient.mvi.ParametrizedStore

internal inline fun <reified T : Store<*, *, *>> DIComponentContext.getStore(
    noinline params: ParametersDefinition? = null
): T {
    if (T::class.java.isAssignableFrom(ParametrizedStore::class.java)) {
        throw IllegalArgumentException("Store with ${T::class} is parametrized and you should use DIComponentContext.getParameterizedStore(param")
    }
    return instanceKeeper.getStore(this::get)
}

internal inline fun <reified Param : Any, reified T : ParametrizedStore<*, *, *, Param>> DIComponentContext.getParameterizedStore(
    crossinline param: () -> Param
): T {
    return instanceKeeper.getStore { get { parametersOf(param()) } }
}

internal inline fun <reified Param : Any, reified T : ParametrizedStore<*, *, *, Param>> DIComponentContext.getParameterizedStore(
    param: Param
): T = getParameterizedStore { param }

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


internal inline fun <reified State : Parcelable, reified T : Store<*, State, *>> DIComponentContext.getStoreSavedState(
    key: String,
): T {
    return getStoreSavedState(
        key,
        save = { it },
        restore = { it }
    )
}

fun <State : Any> Store<*, State, *>.states(lifecycleOwner: LifecycleOwner): androidx.compose.runtime.State<State> {
    return states(lifecycleOwner, mapper = { it })
}

fun <State : Any, T : Any> Store<*, State, *>.states(
    lifecycleOwner: LifecycleOwner,
    mapper: (State) -> T
): androidx.compose.runtime.State<T> {
    return toState(
        lifecycleOwner,
        currentState = state,
        mapper = mapper,
        Store<*, State, *>::states
    )
}


private inline fun <T, R, C> T.toState(
    lifecycleOwner: LifecycleOwner,
    currentState: R,
    crossinline mapper: (R) -> C,
    crossinline subscribe: T.(Observer<R>) -> Disposable
): androidx.compose.runtime.State<C> {
    val state = mutableStateOf(mapper(currentState))
    var disposable: Disposable? = null
    lifecycleOwner.lifecycle.subscribe(
        onCreate = {
            disposable = subscribe(observer(onNext = { state.value = mapper(it) }))
        },
        onDestroy = {
            disposable?.dispose()
        }
    )
    return state
}


//private operator fun ParametersHolder?.plus(parameters: ParametersHolder?): ParametersHolder {
//    if (this == null && parameters == null) return parametersOf()
//    if (this != null && parameters == null) return this
//    if (parameters != null && this == null) return parameters
//    return ParametersHolder((this!!.values + parameters!!.values).toMutableList())
//}