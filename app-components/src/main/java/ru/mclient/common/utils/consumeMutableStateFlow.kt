package ru.mclient.common.utils

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import kotlinx.coroutines.flow.MutableStateFlow

internal inline fun <reified T : Parcelable> StateKeeper.consumeMutableStateFlow(
    key: String,
    initial: () -> T,
): MutableStateFlow<T> {
    val state = MutableStateFlow(consume(key) ?: initial())
    register<Parcelable>(key) { state.value }
    return state
}


internal inline fun <reified T : Parcelable> MutableStateFlow<T>.consumeInStateKeeper(
    key: String,
    stateKeeper: StateKeeper,
): MutableStateFlow<T> {
    stateKeeper.consume<T>(key)?.let(this::value::set)
    stateKeeper.register<Parcelable>(key, this::value)
    return this
}