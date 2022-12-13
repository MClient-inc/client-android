package ru.mclient.ui.service.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mclient.ui.service.profile.ServiceProfilePageState.AnalyticsType.*
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedDropdownMenu
import ru.mclient.ui.view.DesignedDropdownMenuItem
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R


data class ServiceProfilePageState(
    val service: Service?,
    val network: NetworkAnalytics?,
    val company: CompanyAnalytics?,
    val analyticsType: AnalyticsType,
    val isTypeSelecting: Boolean,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {
    data class AnalyticsItem(
        val comeCount: Long,
        val notComeCount: Long,
        val waitingCount: Long,
        val totalRecords: Long,
        val popularity: String,
    )

    data class NetworkAnalytics(
        val id: Long,
        val title: String,
        val analytics: AnalyticsItem,
    )

    data class CompanyAnalytics(
        val id: Long,
        val title: String,
        val analytics: AnalyticsItem,
    )

    data class Service(
        val title: String,
        val description: String,
        val cost: String,
    )


    enum class AnalyticsType {
        COMPANY, NETWORK
    }
}

@Composable
fun ServiceProfilePage(
    state: ServiceProfilePageState,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit,
    onSelect: () -> Unit,
    onToggleCompany: () -> Unit,
    onToggleNetwork: () -> Unit,
    modifier: Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (state.service != null)
            ServiceProfileHeaderComponent(
                service = state.service,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
        if (state.network != null && state.company != null)
            ServiceProfileNetworkAnalyticsItem(
                network = state.network,
                company = state.company,
                isTypeSelecting = state.isTypeSelecting,
                onDismiss = onDismiss,
                onSelect = onSelect,
                onToggleCompany = onToggleCompany,
                onToggleNetwork = onToggleNetwork,
                analyticsType = state.analyticsType,
                modifier = Modifier.fillMaxWidth()
            )
    }
}

@Composable
fun ServiceProfileNetworkAnalyticsItem(
    network: ServiceProfilePageState.NetworkAnalytics,
    company: ServiceProfilePageState.CompanyAnalytics,
    analyticsType: ServiceProfilePageState.AnalyticsType,
    isTypeSelecting: Boolean,
    onDismiss: () -> Unit,
    onSelect: () -> Unit,
    onToggleCompany: () -> Unit,
    onToggleNetwork: () -> Unit,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(
        title = "Аналитика",
        modifier = modifier,
        trailingContent = {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSelect
                        )
                ) {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = analyticsType == NETWORK,
                            enter = fadeIn() + slideInVertically { it / 2 },
                            exit = fadeOut() + slideOutVertically { it / 2 },
                        ) {
                            Text("Сеть")
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = analyticsType == COMPANY,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(),
                        ) {
                            Text("Компания")
                        }
                    }
                    Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                }

                DesignedDropdownMenu(
                    expanded = isTypeSelecting,
                    onDismissRequest = onDismiss,
                    modifier = Modifier.width(250.dp)
                ) {
                    DesignedDropdownMenuItem(
                        text = {
                            Text("Компания")
                        }, trailingIcon = {
                            AnimatedVisibility(analyticsType == COMPANY) {
                                Icon(Icons.Outlined.Check, "выбрано")
                            }
                        },
                        onClick = onToggleCompany
                    )
                    DesignedDropdownMenuItem(
                        text = {
                            Text("Сеть")
                        }, trailingIcon = {
                            AnimatedVisibility(analyticsType == NETWORK) {
                                Icon(Icons.Outlined.Check, "выбрано")
                            }
                        },
                        onClick = onToggleNetwork
                    )
                }
            }
        }) {
        AnalyticsItemBlock(
            when (analyticsType) {
                COMPANY -> company.analytics
                NETWORK -> network.analytics
            },
            Modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyticsItemBlock(
    analytics: ServiceProfilePageState.AnalyticsItem,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp)
                )
            },
            headlineText = {
                Text(text = "${analytics.totalRecords}")
            },
            overlineText = {
                Text(text = "Всего записей")
            }
        )
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp)
                )
            },
            headlineText = {
                if (analytics.popularity.isBlank())
                    Text(
                        text = "Ещё не использовалась", style = LocalTextStyle.current.copy(
                            fontSize = (LocalTextStyle.current.fontSize.value - 1f).sp
                        )
                    )
                else {
                    Text(text = analytics.popularity)
                }
            },
            overlineText = {
                Text(text = "Популярность")
            }
        )
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp),
                    tint = Color.Green
                )
            },
            headlineText = {
                Text(text = "${analytics.comeCount}", color = Color.Green)
            },
            overlineText = {
                Text(text = "Пришли")
            }
        )
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(25.dp),
                    tint = Color.Red
                )
            },
            headlineText = {
                Text(text = "${analytics.notComeCount}", color = Color.Red)
            },
            overlineText = {
                Text(text = "Не пришли")
            }
        )
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color(255, 150, 0, 255)
                )
            },
            headlineText = {
                Text(
                    text = "${analytics.waitingCount}",
                    color = Color(255, 150, 0, 255)
                )
            },
            overlineText = {
                Text(text = "В ожидании")
            }
        )
    }
}

@Composable
fun ServiceProfileHeaderComponent(
    service: ServiceProfilePageState.Service,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.service),
            contentDescription = "иконка услуги",
            modifier = Modifier.size(125.dp)
        )
        Column {
            Text(
                text = service.title,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = service.cost,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = service.description,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedButton(
                text = "Редактировать".toDesignedString(),
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview
@Composable
fun ServiceProfilePagePreview() {
    ServiceProfilePage(
        state = ServiceProfilePageState(
            service = ServiceProfilePageState.Service(
                "Стрижка под ноль",
                "Наш лучший мастер - Дядя Толя всегда к вашим услугам",
                "200 рублей"
            ),
            network = ServiceProfilePageState.NetworkAnalytics(
                id = 1,
                title = "Network-001",
                analytics = ServiceProfilePageState.AnalyticsItem(
                    comeCount = 124,
                    notComeCount = 21,
                    waitingCount = 8,
                    totalRecords = 124 + 21 + 8,
                    popularity = "66%"
                ),
            ),
            company = ServiceProfilePageState.CompanyAnalytics(
                id = 1,
                title = "Company-001",
                analytics = ServiceProfilePageState.AnalyticsItem(
                    comeCount = 60,
                    notComeCount = 12,
                    waitingCount = 2,
                    totalRecords = 60 + 12 + 2,
                    popularity = "34%"
                )
            ),
            analyticsType = COMPANY,
            isTypeSelecting = false,
            isLoading = false,
            isRefreshing = false
        ),
        onEdit = {},
        onRefresh = {},
        onDismiss = {},
        onToggleCompany = {},
        onToggleNetwork = {},
        onSelect = {},
        modifier = Modifier.fillMaxSize()
    )
}