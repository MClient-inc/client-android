package ru.mclient.common.scanner

import ru.mclient.common.Modal

data class ScannerState(
    val isCameraEnabled: Boolean,
    val isLoading: Boolean,
)

interface Scanner : Modal {

    val state: ScannerState

    fun onRecognize(value: String)

}