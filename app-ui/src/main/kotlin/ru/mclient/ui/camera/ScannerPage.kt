package ru.mclient.ui.camera

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.mclient.ui.view.camera.CameraPreview
import ru.mclient.ui.view.camera.rememberCameraState
import ru.mclient.ui.view.camera.rememberImageRecognizer

class CameraPageState(
    val isCameraEnabled: Boolean,
    val isLoading: Boolean,
)

@OptIn(ExperimentalPermissionsApi::class, ExperimentalTextApi::class)
@Composable
fun ScannerPage(
    state: CameraPageState,
    onRecognize: (String) -> Unit,
    modifier: Modifier,
) {
    val permissions = rememberPermissionState(permission = Manifest.permission.CAMERA)
    when (permissions.status) {
        is PermissionStatus.Denied -> Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            Text("Необходим доступ к камере")
            OutlinedButton(onClick = { permissions.launchPermissionRequest() }) {
                Text("Продолжить")
            }
        }

        is PermissionStatus.Granted -> CameraPreview(
            cameraState = rememberCameraState(state.isCameraEnabled),
            recognizer = rememberImageRecognizer(onRecognize = onRecognize),
            modifier = modifier,
        )
    }
}