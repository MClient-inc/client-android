package ru.mclient.ui.service.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.service.create.ServiceCreate
import ru.mclient.common.service.create.ServiceCreateState

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
    ServiceCreatePage(
        modifier = modifier,
        state = component.state.toUI(),
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