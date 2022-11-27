package ru.mclient.common.scanner

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.mclient.common.ModalState

class ScannerComponent(
    private val onClientProfile: (Long) -> Unit,
) : Scanner {

    override val state: ScannerState get() = ScannerState(modalState.isVisible, false)

    private val regex =
        "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.\\S{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.\\S{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.\\S{2,}|www\\.[a-zA-Z0-9]+\\.\\S{2,})/clients/".toRegex()

    override fun onRecognize(value: String) {
        Log.d("ScannerLogger", "scanned: $value")
        Log.d("ScannerLogger", "scanned: ${value.replace(regex, "")}")
        if (regex in value) {
            modalState = modalState.copy(isVisible = false)
            val clientId = value.replace(regex, "").removeSuffix("?source=qrcode")
            onClientProfile.invoke(clientId.toLongOrNull() ?: return)
        }
    }

    override var modalState: ModalState by mutableStateOf(ModalState(false))

    override fun updateState(isVisible: Boolean) {
        modalState = modalState.copy(isVisible)
    }

}