package ru.mclient.ui.record.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            modifier = modifier
        )
    }
}