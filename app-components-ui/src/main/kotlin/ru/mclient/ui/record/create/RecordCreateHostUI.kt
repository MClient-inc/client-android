package ru.mclient.ui.record.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.common.record.create.RecordCreateHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.record.profile.toMoney
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.outlined

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCreateHostUI(
    component: RecordCreateHost,
    modifier: Modifier,
) {
    TopBarHostUI(component = component, modifier = modifier) {
        val scrollState = rememberScrollState()
        Scaffold(
            snackbarHost = {
                AnimatedVisibility(
                    visible = component.state.isButtonAvailable && !scrollState.isScrollInProgress,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 1.dp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .outlined()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                "Итого: ${component.state.totalCost.toMoney()}",
                                modifier = Modifier.align(Alignment.End)
                            )
                            DesignedButton(
                                text = "Записать",
                                onClick = component::onContinue,
                                enabled = component.state.isButtonAvailable,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
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
                    if (component.state.isButtonAvailable)
                        Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}
