package ru.mclient.ui.record.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedCreateItem
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class RecordCreateAbonementsSelectorBlockState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val selectedAbonements: Map<Int, ClientAbonement>,
    val clientAbonements: List<ClientAbonement>,
    val isRefreshing: Boolean,
) {
    class ClientAbonement(
        val id: Long,
        val abonement: Abonement,
        val usages: Int,
    )

    data class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )

    data class Subabonement(
        val id: Long,
        val title: String,
        val maxUsages: Int,
        val cost: Long,
    )
}


@Composable
fun RecordCreateAbonementsSelectorBlock(
    state: RecordCreateAbonementsSelectorBlockState,
    onSelect: (RecordCreateAbonementsSelectorBlockState.ClientAbonement) -> Unit,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedTitledBlock(
        title = "Абонементы",
        button = "Выбрать",
        onClick = onExpand,
        modifier = modifier,
    ) {
        RecordCreateSelectedAbonementsList(
            abonements = state.selectedAbonements,
            onCreate = onExpand,
            onDelete = onDelete,
            modifier = Modifier.fillMaxWidth(),
        )
        DesignedDropdownMenu(
            expanded = state.isExpanded,
            onDismissRequest = onDismiss
        ) {
            RecordCreateClientAbonementsItem(
                abonements = state.clientAbonements,
                onSelect = onSelect,
                refreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}


@Composable
fun RecordCreateSelectedAbonementsList(
    abonements: Map<Int, RecordCreateAbonementsSelectorBlockState.ClientAbonement>,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (abonements.isEmpty()) {
            NoRecordCreate(onCreate = onCreate, modifier = Modifier.fillMaxWidth())
        } else {
            abonements.forEach { (uniqueId, abonement) ->
                RecordCreateSelectedAbonementItem(
                    item = abonement,
                    onDelete = { onDelete(uniqueId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RecordCreateClientAbonementsItem(
    abonements: List<RecordCreateAbonementsSelectorBlockState.ClientAbonement>,
    onSelect: (RecordCreateAbonementsSelectorBlockState.ClientAbonement) -> Unit,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = refreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (abonements.isEmpty()) {
            DesignedCreateItem(
                text = "Нет абонементов",
                icon = painterResource(id = R.drawable.empty),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            abonements.forEach { abonement ->
                RecordCreateClientAbonementItem(
                    item = abonement,
                    onSelect = { onSelect(abonement) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordCreateClientAbonementItem(
    item: RecordCreateAbonementsSelectorBlockState.ClientAbonement,
    onSelect: () -> Unit,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.subabonements),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        },
        headlineText = {
            Text(item.abonement.title)
        },
        supportingText = {
            Text(item.abonement.subabonement.title)
        },
        trailingContent = {
            Text(
                "${item.usages}/${item.abonement.subabonement.maxUsages}",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier
            .outlined()
            .clickable(onClick = onSelect),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordCreateSelectedAbonementItem(
    item: RecordCreateAbonementsSelectorBlockState.ClientAbonement,
    onDelete: () -> Unit,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.subabonements),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        },
        overlineText = {
            Text("${item.usages}/${item.abonement.subabonement.maxUsages}")
        },
        headlineText = {
            Text(item.abonement.title)
        },
        supportingText = {
            Text(item.abonement.subabonement.title)
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
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
    DesignedCreateItem(
        text = "Пока пусто",
        icon = painterResource(id = R.drawable.empty),
        onClick = onCreate,
        isAvailable = true,
        modifier = modifier.outlined(),
    )
}
