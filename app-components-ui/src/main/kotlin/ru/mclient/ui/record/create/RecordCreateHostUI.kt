package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.create.RecordCreateHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.toDesignedString

@Composable
fun RecordCreateHostUI(
    component: RecordCreateHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component, modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RecordCreateClientsSelectorUI(
                    component = component.clientsSelector,
                    modifier = Modifier.fillMaxWidth(),
                )
                RecordCreateDateSelectorUI(
                    component = component.dateSelector,
                    modifier = Modifier.fillMaxWidth(),
                )
                RecordCreateTimeSelectorUI(
                    component = component.timeSelector,
                    modifier = Modifier.fillMaxWidth(),
                )
                RecordCreateStaffSelectorUI(
                    component = component.staffSelector,
                    modifier = Modifier.fillMaxWidth(),
                )
                RecordCreateServicesSelectorUI(
                    component = component.servicesSelector,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            DesignedButton(
                text = "Записать".toDesignedString(),
                onClick = component::onContinue,
                enabled = component.state.isButtonAvailable,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
