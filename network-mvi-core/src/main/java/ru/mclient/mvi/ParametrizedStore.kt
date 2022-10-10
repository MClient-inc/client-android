package ru.mclient.mvi

import com.arkivanov.mvikotlin.core.store.Store

interface ParametrizedStore<in Intent : Any, out State : Any, out Label : Any, in Params: Any>: Store<Intent, State, Label>