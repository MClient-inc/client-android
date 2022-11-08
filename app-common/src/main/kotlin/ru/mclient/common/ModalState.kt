package ru.mclient.common

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

data class ModalState(val isShown: Boolean)

interface Modal {

    val modalState: ModalState

    fun updateState(isVisible: Boolean)

}

interface StackModal<Child : Any> : Modal {

    val modalStack: Value<ChildStack<*, Child>>

}