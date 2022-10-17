package ru.mclient.ui.staff.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mclient.common.staff.create.StaffCreate
import ru.mclient.common.staff.create.StaffCreateState

fun StaffCreateState.toUI(): StaffCreatePageState {
    return StaffCreatePageState(
        username = name,
        codename = codename,
        role = role,
        isLoading = false,
        error = if (isError) "Непредвиденная ошибка" else null,
        isButtonsEnabled = isButtonsEnabled,
    )
}

@Composable
fun StaffCreateUI(component: StaffCreate, modifier: Modifier) {
    val state by component.state
    StaffCreatePage(
        modifier = modifier,
        state = state.toUI(),
        onUpdate = {
            component.onUpdate(
                username = it.username,
                codename = it.codename,
                role = it.role
            )
        },
        onCreate = {
            component.onCreate(
                username = it.username,
                codename = it.codename,
                role = it.role
            )
        }
    )
}