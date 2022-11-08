package ru.mclient.ui.client.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.QRCode

class ClientQRCodeBlockState(
    val code: String?,
    val isLoading: Boolean,
    val isShareAvailable: Boolean,
)

@Composable
fun ClientQRCodeBlock(
    state: ClientQRCodeBlockState,
    onShare: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.code != null)
            QRCode(
                text = state.code,
                modifier = Modifier.size(250.dp)
            )
        else
            Box(modifier = Modifier.size(250.dp))
        OutlinedButton(onClick = onShare, enabled = state.isShareAvailable) {
            Text(text = "Поделиться")
        }
    }
}