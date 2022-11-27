package ru.mclient.ui.scanner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.scanner.Scanner
import ru.mclient.common.scanner.ScannerState
import ru.mclient.ui.ModalUI
import ru.mclient.ui.camera.CameraPageState
import ru.mclient.ui.camera.ScannerPage

private fun ScannerState.toUI(): CameraPageState {
    return CameraPageState(isCameraEnabled, isLoading)
}

@Composable
fun ScannerUI(
    component: Scanner,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    ModalUI(
        component = component,
        sheetContent = {
            ScannerPage(
                state = component.state.toUI(),
                onRecognize = component::onRecognize,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
        },
        showDivider = false,
        content = content,
        modifier = modifier,
    )
}