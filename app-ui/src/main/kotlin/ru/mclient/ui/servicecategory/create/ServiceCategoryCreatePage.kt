package ru.mclient.ui.servicecategory.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedString
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class ServiceCategoryCreatePageState(
    val title: String,
    val isLoading: Boolean,
    val error: DesignedString?
)

class ServiceCategoryCreatePageInput(
    val title: String,
)

fun ServiceCategoryCreatePageState.toInput(
    title: String = this.title,
): ServiceCategoryCreatePageInput {
    return ServiceCategoryCreatePageInput(
        title = title,
    )
}

@Composable
fun ServiceCategoryCreatePage(
    state: ServiceCategoryCreatePageState,
    onUpdate: (ServiceCategoryCreatePageInput) -> Unit,
    onCreate: (ServiceCategoryCreatePageInput) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DesignedTextField(
            value = state.title,
            onValueChange = {
                onUpdate(state.toInput(title = it))
            },
            label = stringResource(R.string.company_service_category_create_name),
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedButton(
            text = stringResource(R.string.company_service_category_add).toDesignedString(),
            onClick = { onCreate(state.toInput()) }
        )
    }
}

@Preview
@Composable
fun ServiceCreatePreview(
) {
    var state by remember {
        mutableStateOf(
            ServiceCategoryCreatePageState(
                title = "",
                isLoading = false,
                error = null
            )
        )
    }

    ServiceCategoryCreatePage(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUpdate = {
            state = state.copy(
                title = it.title,
            )
        },
        onCreate = {}
    )
}