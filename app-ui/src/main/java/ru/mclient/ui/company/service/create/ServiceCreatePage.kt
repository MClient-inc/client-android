package ru.mclient.ui.company.service.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedTextButton
import ru.mclient.ui.view.DesignedTextField
import ru.shafran.ui.R

data class ServiceCreatePageState(
    val serviceName: String,
    val description: String,
    val cost: Int,
    val isLoading: Boolean,
    val error: String?
)

class ServiceCreatePageInput(
    val serviceName: String,
    val description: String,
    val cost: Int
)

fun ServiceCreatePageState.toInput(
    serviceName: String = this.serviceName,
    description: String = this.description,
    cost: Int = this.cost
): ServiceCreatePageInput {
    return ServiceCreatePageInput(
        serviceName = serviceName,
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
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

//        service name
        DesignedTextField(
            value = state.serviceName,
            onValueChange = {
                onUpdate(state.toInput(serviceName = it))
            },
            label = stringResource(R.string.company_service_create_name),
            maxLines = 1,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

//        service description
        DesignedTextField(
            value = state.description,
            onValueChange = {
                onUpdate(state.toInput(description = it))
            },
            label = stringResource(R.string.company_service_create_description),
            maxLines = 3,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

//        service cost
        DesignedTextField(
            value = state.cost.toString(),
            onValueChange = {
//                onUpdate(state.toInput(cost = it.toInt())) TODO()
            },
            label = stringResource(R.string.company_service_cost),
            maxLines = 3,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

//        create service

        DesignedTextButton(
            text = stringResource(R.string.company_service_add),
            onClick = { onCreate(state.toInput()) }
        )

    }
}

//@Preview
//@Composable
//fun WorkerCreatePage1(
//) {
//
//    val bebra = remember { mutableStateOf(ServiceCreatePageState(
//        serviceName = "",
//        description = "",
//        cost = 0,
//        isLoading = false,
//        error = null
//    )) }
//
//    ServiceCreatePage(
//        modifier = Modifier.fillMaxSize(),
//        state = bebra.value,
//        onUpdate = {
//            bebra.value = bebra.value.copy(
//                serviceName = it.serviceName,
//                description = it.description,
//                cost = it.cost
//            )
//        },
//        onCreate = {
//
//        }
//    )
//}