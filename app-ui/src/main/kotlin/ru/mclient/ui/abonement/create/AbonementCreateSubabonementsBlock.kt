package ru.mclient.ui.abonement.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.abonement.profile.formatToUsages
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class AbonementCreateSubabonementsBlockState(
    val subabonements: List<Subabonement>,
    val creation: SubabonementCreation,
) {

    data class Subabonement(
        val title: String,
        val usage: String,
        val cost: String,
        val uniqueId: Int,
    )

    data class SubabonementCreation(
        val title: String,
        val usages: String,
        val cost: String,
        val isAvailable: Boolean,
        val isButtonAvailable: Boolean,
    )

}

data class SubabonementInput(
    val title: String,
    val usage: String,
    val cost: String,
)

fun AbonementCreateSubabonementsBlockState.SubabonementCreation.toInput(
    title: String = this.title,
    usages: String = this.usages,
    cost: String = this.cost,
): SubabonementInput {
    return SubabonementInput(
        title = title,
        usage = usages,
        cost = cost,
    )
}

@Composable
fun AbonementCreateSubabonementsBlock(
    state: AbonementCreateSubabonementsBlockState,
    onCreate: () -> Unit,
    onUpdate: (SubabonementInput) -> Unit,
    onDelete: (AbonementCreateSubabonementsBlockState.Subabonement) -> Unit,
    modifier: Modifier,
) {
    DesignedTitledBlock(title = "Подабонементы", modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.subabonements.forEach {
                SubabonementItem(
                    subabonement = it,
                    onDelete = onDelete,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AnimatedVisibility(visible = state.creation.isAvailable) {
                SubabonementCreation(
                    creation = state.creation,
                    onUpdate = onUpdate,
                    onCreate = onCreate,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun SubabonementCreation(
    creation: AbonementCreateSubabonementsBlockState.SubabonementCreation,
    onUpdate: (SubabonementInput) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier,
) {
    val config = LocalConfiguration.current
    if (config.screenWidthDp >= 560) {
        Row(modifier = modifier) {
            DesignedTextField(
                value = creation.title,
                onValueChange = { onUpdate(creation.toInput(title = it)) },
                placeholder = "Название",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f, fill = true)
            )
            DesignedTextField(
                value = creation.usages,
                onValueChange = { onUpdate(creation.toInput(usages = it)) },
                placeholder = "Кол-во использований",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f, fill = true)
            )
            DesignedTextField(
                value = creation.cost,
                onValueChange = { onUpdate(creation.toInput(cost = it)) },
                placeholder = "Стоимость",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f, fill = true)
            )
            IconButton(
                onClick = onCreate,
                enabled = creation.isButtonAvailable,
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Добавить")
            }
        }
    } else {
        Column(modifier = modifier) {
            DesignedTextField(
                value = creation.title,
                onValueChange = { onUpdate(creation.toInput(title = it)) },
                placeholder = "Название",
                modifier = Modifier.fillMaxWidth()
            )
            DesignedTextField(
                value = creation.usages,
                onValueChange = { onUpdate(creation.toInput(usages = it)) },
                placeholder = "Кол-во использований",
                modifier = Modifier.fillMaxWidth()
            )
            DesignedTextField(
                value = creation.cost,
                onValueChange = { onUpdate(creation.toInput(cost = it)) },
                placeholder = "Стоимость",
                modifier = Modifier.fillMaxWidth()
            )
            DesignedButton(
                text = "Добавить",
                onClick = onCreate,
                enabled = creation.isButtonAvailable,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubabonementItem(
    subabonement: AbonementCreateSubabonementsBlockState.Subabonement,
    onDelete: (AbonementCreateSubabonementsBlockState.Subabonement) -> Unit,
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
            Text(subabonement.title)
        },
        supportingText = {
            Text(subabonement.usage.toInt().formatToUsages())
        },
        trailingContent = {
            IconButton(onClick = { onDelete(subabonement) }) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
            }
        },
        modifier = modifier.outlined(),
    )
}