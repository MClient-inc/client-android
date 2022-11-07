package ru.mclient.ui.abonement.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.abonement.list.AbonementsListHost
import ru.mclient.ui.bar.MergedHostUI

@Composable
fun AbonementsListHostUI(component: AbonementsListHost, modifier: Modifier) {
    MergedHostUI(
        component = component,
        modifier = modifier,
    ) {
        AbonementsListUI(
            component = component.abonementsList,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        )
    }
}