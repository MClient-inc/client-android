package ru.mclient.ui.abonnement.profile

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

data class AbonnementProfilePageState(
    val abonnement: Abonnement?,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) {

    data class Abonnement(
        val name: String,
        val subAbonnements: List<SubAbonnement>,
        val services: List<Service>,
    )

    data class SubAbonnement(
        val name: String,
        val maxTimesNumberToUse: Int
    )

    data class Service(
        val title: String,
        val cost: Long
    )

}

@Composable
fun AbonnementProfilePage(
    state: AbonnementProfilePageState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = { onRefresh() },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (state.abonnement != null) {
            AbonnementProfileAbonnementComponent(
                abonnement = state.abonnement,
                modifier = Modifier.fillMaxWidth()
            )
            AbonnementProfileSubAbonnementComponent(
                subAbonnements = state.abonnement.subAbonnements,
                modifier = Modifier.fillMaxWidth()
            )
            AbonnementProfileServicesComponent(
                abonnementServices = state.abonnement.services,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbonnementProfileAbonnementComponent(
    abonnement: AbonnementProfilePageState.Abonnement,
    modifier: Modifier
) {
    ListItem(
        headlineText = {
            Text(abonnement.name)
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
                painter = painterResource(id = R.drawable.abonnement),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),

        )
}

@Composable
fun AbonnementProfileSubAbonnementComponent(
    subAbonnements: List<AbonnementProfilePageState.SubAbonnement>,
    modifier: Modifier
) {
    DesignedTitledBlock(
        title = "Подабонементы",
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            subAbonnements.forEach { subAbonnement ->
                SubAbonnementCardComponent(
                    subAbonnement = subAbonnement,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubAbonnementCardComponent(
    subAbonnement: AbonnementProfilePageState.SubAbonnement,
    modifier: Modifier
) {
    ListItem(
        headlineText = {
            Text(subAbonnement.name)
        },
        supportingText = {
            Text(
                format(
                    maxTimesNumberToUse = subAbonnement.maxTimesNumberToUse
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
                painter = painterResource(id = R.drawable.subabonnements),
                contentDescription = "иконка",
                modifier = Modifier.size(50.dp),
            )
        },
        modifier = modifier.outlined(),
    )
}

@Composable
fun AbonnementProfileServicesComponent(
    abonnementServices: List<AbonnementProfilePageState.Service>,
    modifier: Modifier
) {
    DesignedTitledBlock(
        title = "Связанные услуги",
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            abonnementServices.forEach { service ->
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
    service: AbonnementProfilePageState.Service,
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
fun AbonnementProfilePagePreview() {
    AbonnementProfilePage(
        state = AbonnementProfilePageState(
            abonnement = AbonnementProfilePageState.Abonnement(
                name = "Парикмахерская",
                services = listOf(
                    AbonnementProfilePageState.Service(
                        title = "Стрижка модельная",
                        cost = 350
                    ),
                    AbonnementProfilePageState.Service(
                        title = "Покраска волос в ярко-красный",
                        cost = 600
                    )
                ),
                subAbonnements = listOf(
                    AbonnementProfilePageState.SubAbonnement(
                        name = "Стрижка и покраска волос",
                        maxTimesNumberToUse = 5
                    ),
                    AbonnementProfilePageState.SubAbonnement(
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