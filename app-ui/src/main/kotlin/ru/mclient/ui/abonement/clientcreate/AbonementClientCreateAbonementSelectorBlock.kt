package ru.mclient.ui.abonement.clientcreate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.mclient.ui.view.DesignedCreateItem
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R

data class AbonementClientCreateAbonementSelectorBlockState(
    val isExpanded: Boolean,
    val isAvailable: Boolean,
    val abonement: Abonement?,
) {

    data class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )

    data class Subabonement(
        val id: Long,
        val title: String,
    )

}


@Composable
fun AbonementClientCreateAbonementSelectorBlock(
    state: AbonementClientCreateAbonementSelectorBlockState,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dropdownContent: @Composable () -> Unit,
) {
    DesignedOutlinedTitledBlock(
        title = "Абонемент",
        modifier = modifier,
    ) {
        AbonementSelector(
            abonement = state.abonement,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = state.isAvailable, onClick = onExpand),
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
fun AbonementSelector(
    abonement: AbonementClientCreateAbonementSelectorBlockState.Abonement?,
    modifier: Modifier = Modifier,
) {
    if (abonement == null) {
        NoAbonement(modifier = modifier)
    } else {
        AbonementItem(
            item = abonement,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AbonementItem(
    item: AbonementClientCreateAbonementSelectorBlockState.Abonement,
    modifier: Modifier,
) {
    DesignedCreateItem(
        text = item.title,
        supportingText = item.subabonement.title,
        icon = painterResource(id = R.drawable.subabonements),
        modifier = modifier,
    )
}

@Composable
fun NoAbonement(
    modifier: Modifier,
) {
    DesignedCreateItem(
        text = "Пока не выбран",
        icon = painterResource(id = R.drawable.empty),
        modifier = modifier,
    )
}
