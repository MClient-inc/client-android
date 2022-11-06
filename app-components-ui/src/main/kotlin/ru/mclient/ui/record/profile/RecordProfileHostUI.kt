package ru.mclient.ui.record.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.profile.RecordProfileHost
import ru.mclient.common.staff.create.StaffCreateHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.staff.create.StaffCreateUI

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
            component = component,
            modifier = modifier
        )
    }
}