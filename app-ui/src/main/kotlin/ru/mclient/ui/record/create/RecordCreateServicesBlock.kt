package ru.mclient.ui.record.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class RecordCreateServicesBlockState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val selectedServices: List<Service>,
) {
    class Service(
        val id: Long,
        val title: String,
        val cost: Long,
        val formattedCost: String,
    )
}


@Composable
fun RecordCreateServicesBlock(
    state: RecordCreateServicesBlockState,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: (RecordCreateServicesBlockState.Service) -> Unit,
    modifier: Modifier = Modifier,
    dropdownContent: @Composable () -> Unit,
) {
    DesignedTitledBlock(
        title = "Услуги",
        button = "Добавить",
        onClick = onExpand,
        modifier = modifier,
    ) {
        RecordCreateServiceList(
            state.selectedServices,
            onCreate = onExpand,
            onDelete = onDelete,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedDropdownMenu(
            expanded = state.isExpanded,
            onDismissRequest = onDismiss
        ) {
            dropdownContent()
        }
    }
}

@Composable
fun RecordCreateServiceList(
    services: List<RecordCreateServicesBlockState.Service>,
    onCreate: () -> Unit,
    onDelete: (RecordCreateServicesBlockState.Service) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (services.isEmpty()) {
            NoRecordCreate(onCreate = onCreate, modifier = Modifier.fillMaxWidth())
        } else {
            services.forEach {
                RecordCreateServiceItem(
                    item = it,
                    onDelete = onDelete,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCreateServiceItem(
    item: RecordCreateServicesBlockState.Service,
    onDelete: (RecordCreateServicesBlockState.Service) -> Unit,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.service),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        },
        headlineText = {
            Text(item.title)
        },
        supportingText = {
            Text(item.formattedCost)
        },
        trailingContent = {
            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
            }
        },
        modifier = modifier.outlined(),
    )
}

@Composable
fun NoRecordCreate(
    onCreate: () -> Unit,
    modifier: Modifier,
) {
    RecordCreateItem(
        text = "Пока пусто",
        icon = painterResource(id = R.drawable.empty),
        onClick = onCreate,
        isAvailable = true,
        modifier = modifier.outlined(),
    )
}