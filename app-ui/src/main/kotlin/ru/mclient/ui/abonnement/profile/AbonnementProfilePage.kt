package ru.mclient.ui.abonement.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toMoney
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class AbonementProfilePageState(
    val abonement: Abonement?,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {

    data class Abonement(
        val name: String,
        val subabonements: List<SubAbonement>,
        val services: List<Service>,
    )

    data class SubAbonement(
        val name: String,
        val maxTimesNumberToUse: Int,
    )

    data class Service(
        val title: String,
        val cost: Long
    )

}

@Composable
fun AbonementProfilePage(
    state: AbonementProfilePageState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (state.abonement != null) {
            AbonementProfileAbonementComponent(
                abonement = state.abonement,
                modifier = Modifier.fillMaxWidth()
            )
            AbonementProfileSubAbonementComponent(
                subAbonements = state.abonement.subabonements,
                modifier = Modifier.fillMaxWidth()
            )
            AbonementProfileServicesComponent(
                abonementServices = state.abonement.services,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbonementProfileAbonementComponent(
    abonement: AbonementProfilePageState.Abonement,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(abonement.name)
        },
        supportingText = {

        },
        trailingContent = {
            Icon(
                Icons.Outlined.ArrowForward,
                contentDescription = "иконка",
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.abonement),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),

        )
}

@Composable
fun AbonementProfileSubAbonementComponent(
    subAbonements: List<AbonementProfilePageState.SubAbonement>,
    modifier: Modifier,
) {
    DesignedTitledBlock(
        title = "Подабонементы",
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            subAbonements.forEach { subAbonement ->
                SubAbonementCardComponent(
                    subAbonement = subAbonement,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubAbonementCardComponent(
    subAbonement: AbonementProfilePageState.SubAbonement,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(subAbonement.name)
        },
        supportingText = {
            Text(
                format(
                    maxTimesNumberToUse = subAbonement.maxTimesNumberToUse
                )
            )
        },
        trailingContent = {
            Icon(
                Icons.Outlined.ArrowForward,
                contentDescription = "иконка",
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.subabonements),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),
    )
}

@Composable
fun AbonementProfileServicesComponent(
    abonementServices: List<AbonementProfilePageState.Service>,
    modifier: Modifier,
) {
    DesignedTitledBlock(
        title = "Связанные услуги",
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            abonementServices.forEach { service ->
                ServiceCardComponent(
                    service,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCardComponent(
    service: AbonementProfilePageState.Service,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(service.title)
        },
        supportingText = {
            Text(service.cost.toMoney())
        },
        trailingContent = {
            Icon(
                Icons.Outlined.ArrowForward,
                contentDescription = "иконка",
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.service),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),
    )
}

fun format(maxTimesNumberToUse: Int): String {
    return "$maxTimesNumberToUse раз"
}

@Preview
@Composable
fun AbonementProfilePagePreview() {
    AbonementProfilePage(
        state = AbonementProfilePageState(
            abonement = AbonementProfilePageState.Abonement(
                name = "Парикмахерская",
                services = listOf(
                    AbonementProfilePageState.Service(
                        title = "Стрижка модельная",
                        cost = 350
                    ),
                    AbonementProfilePageState.Service(
                        title = "Покраска волос в ярко-красный",
                        cost = 600
                    )
                ),
                subabonements = listOf(
                    AbonementProfilePageState.SubAbonement(
                        name = "Стрижка и покраска волос",
                        maxTimesNumberToUse = 5
                    ),
                    AbonementProfilePageState.SubAbonement(
                        name = "Массаж",
                        maxTimesNumberToUse = 10
                    )
                ),
            ),
            isRefreshing = false,
            isLoading = false
        ),
        onRefresh = { /*TODO*/ }
    )
}