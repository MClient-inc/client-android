package ru.mclient.ui.record.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.record.list.RecordsListHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.view.ExtendOnShowFloatingActionButton
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString

@Composable
fun RecordsListHostUI(
    component: RecordsListHost,
    modifier: Modifier,
) {
    TopBarHostUI(
        component = component,
        floatingActionButton = {
            ExtendOnShowFloatingActionButton(
                text = "Добавить запись".toDesignedString(),
                icon = Icons.Outlined.Add.toDesignedDrawable(),
                isShown = true,
                onClick = component::onRecordCreate,
            )
        },
        modifier = modifier,
    ) {
        RecordsListUI(component = component.recordsList, modifier = Modifier.fillMaxSize())
    }
}