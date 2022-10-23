package ru.mclient.ui.service.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class ServiceCreatePageState(
    val title: String,
    val description: String,
    val cost: String,
    val isLoading: Boolean,
    val error: String?
)

class ServiceCreatePageInput(
    val serviceName: String,
    val description: String,
    val cost: String
)

fun ServiceCreatePageState.toInput(
    title: String = this.title,
    description: String = this.description,
    cost: String = this.cost
): ServiceCreatePageInput {
    return ServiceCreatePageInput(
        serviceName = title,
        description = description,
        cost = cost
    )
}

@Composable
fun ServiceCreatePage(
    modifier: Modifier,
    state: ServiceCreatePageState,
    onUpdate: (ServiceCreatePageInput) -> Unit,
    onCreate: (ServiceCreatePageInput) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DesignedTextField(
            value = state.title,
            onValueChange = {
                onUpdate(state.toInput(title = it))
            },
            label = stringResource(R.string.company_service_create_name),
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedTextField(
            value = state.cost,
            onValueChange = {
                onUpdate(state.toInput(cost = it))
            },
            label = stringResource(R.string.company_service_cost),
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        DesignedTextField(
            value = state.description,
            onValueChange = {
                onUpdate(state.toInput(description = it))
            },
            label = stringResource(R.string.company_service_create_description),
            maxLines = 5,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedButton(
            text = stringResource(R.string.company_service_add).toDesignedString(),
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
            ServiceCreatePageState(
                title = "",
                description = "",
                cost = "0",
                isLoading = false,
                error = null
            )
        )
    }

    ServiceCreatePage(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUpdate = {
            state = state.copy(
                title = it.serviceName,
                description = it.description,
                cost = it.cost.filter(Char::isDigit)
            )
        },
        onCreate = {}
    )
}