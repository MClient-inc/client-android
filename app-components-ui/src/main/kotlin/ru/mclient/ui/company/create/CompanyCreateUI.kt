package ru.mclient.ui.company.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.company.create.CompanyCreate
import ru.mclient.common.company.create.CompanyCreateState

fun CompanyCreateState.toUI(): CompanyCreatePageState {
    return CompanyCreatePageState(
        title = title,
        codename = codename,
        description = description,
        isLoading = false,
        error = if (isError) "Непредвиденная ошибка" else null
    )
}

@Composable
fun CompanyCreateUI(component: CompanyCreate, modifier: Modifier) {
    CompanyCreatePage(
        modifier = modifier,
        state = component.state.toUI(),
        onUpdate = {
            component.onUpdate(
                title = it.title,
                codename = it.codename,
                description = it.description
            )
        },
        onCreate = {
            component.onCreate(
                title = it.title,
                codename = it.codename,
                description = it.description
            )
        }
    )
}