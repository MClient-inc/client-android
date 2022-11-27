package ru.mclient.common.modal

import ru.mclient.common.ModalState
import ru.mclient.mvi.modal.ModalStore

fun ModalStore.State.toState(): ModalState {
    return ModalState(isVisible)
}
