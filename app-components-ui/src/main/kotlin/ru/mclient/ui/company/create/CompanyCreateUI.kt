package ru.mclient.ui.company.create

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
    val state = component.state.collectAsState().value.toUI()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CompanyCreatePage(
            modifier = modifier,
            state = state,
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
}