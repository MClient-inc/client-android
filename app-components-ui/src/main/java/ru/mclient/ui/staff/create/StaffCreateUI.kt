package ru.mclient.ui.staff.create

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.create.StaffCreate
import ru.mclient.common.staff.create.StaffCreateState

fun StaffCreateState.toUI(): StaffCreatePageState {
    return StaffCreatePageState(
        username = username,
        codename = codename,
        isLoading = false,
        error = if (isError) "Непредвиденная ошибка" else null
    )
}

@Composable
fun StaffCreateUI(component : StaffCreate, modifier: Modifier) {
    val state = component.state.collectAsState().value.toUI()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        StaffCreatePage(
            modifier = modifier,
            state = state,
            onUpdate = {
                component.onUpdate(
                    username = it.username,
                    codename = it.codename
                )
            },
            onCreate = {
                component.onCreate(
                    username = it.username,
                    codename = it.codename
                )
            }
        )
    }
}