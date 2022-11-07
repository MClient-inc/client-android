package ru.mclient.ui.abonement.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ru.mclient.common.abonement.create.AbonementCreateHost
import ru.mclient.ui.bar.TopBarHostUI
import ru.mclient.ui.service.list.ServicesListSelectorUI
import ru.mclient.ui.view.outlined

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbonementCreateHostUI(
    component: AbonementCreateHost,
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
                        onClick = component::onContinue,
                        shape = MaterialTheme.shapes.medium,
                        enabled = component.state.isButtonAvailable,
                        tonalElevation = 1.dp,
                        modifier = Modifier
                            .outlined()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Добавить", modifier = Modifier.padding(12.5.dp))
                        }
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AbonementCreateProfileUI(
                        component = component.profile,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AbonementCreateSubabonementsUI(
                        component = component.subabonements,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ServicesListSelectorUI(
                        component = component.services,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (component.state.isButtonAvailable)
                        Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}