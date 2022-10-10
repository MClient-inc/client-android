package ru.mclient.ui.company.service.create

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.mclient.common.company.service.create.ServiceCreate
import ru.mclient.common.company.service.create.ServiceCreateState

fun ServiceCreateState.toUI(): ServiceCreatePageState {
    return ServiceCreatePageState(
        serviceName = serviceName,
        description = description,
        cost = cost,
        isLoading = false,
        error = if (isError) "Непредвиденная ошибка" else null
    )
}

@Composable
fun ServiceCreateUI(component: ServiceCreate, modifier: Modifier) {
    val state = component.state.collectAsState().value.toUI()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ServiceCreatePage(
            modifier = modifier,
            state = state,
            onUpdate = {
                component.onUpdate(
                    serviceName = it.serviceName,
                    description = it.description,
                    cost = it.cost
                )
            },
            onCreate = {
                component.onCreate(
                    serviceName = it.serviceName,
                    description = it.description,
                    cost = it.cost
                )
            }
        )
    }
}