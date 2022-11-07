package ru.mclient.ui.record.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.profile.RecordProfileHost
import ru.mclient.ui.bar.TopBarHostUI

@Composable
fun RecordProfileHostUI(
    component: RecordProfileHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        modifier = modifier,
    ) {
        RecordProfileUI(
            component = component.recordProfile,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        )
    }
}