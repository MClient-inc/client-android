package ru.mclient.ui.service.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R


data class ServiceProfilePageState(
    val service: Service?,
//    val network: NetworkAnalytics?,
//    val company: CompanyAnalytics?,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) {
    data class AnalyticsItem(
        val comeCount: Long,
        val notComeCount: Long,
        val waitingCount: Long,
        val totalRecords: Long,
        val value: String
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
        val cost: String
    )
}

@Composable
fun ServiceProfilePage(
    state: ServiceProfilePageState,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier
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
//        if (state.network != null)
//            ServiceProfileNetworkAnalyticsItem(
//                network = state.network,
//                modifier = Modifier.fillMaxWidth()
//            )
//        if (state.company != null)
//            ServiceProfileCompanyAnalyticsItem(
//                company = state.company,
//                modifier = Modifier.fillMaxWidth()
//            )
    }
}

@Composable
fun ServiceProfileNetworkAnalyticsItem(
    network: ServiceProfilePageState.NetworkAnalytics,
    modifier: Modifier
) {
    DesignedOutlinedTitledBlock(title = "Сеть", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            NetworkAnalyticsItemBlock(
                analytics = network.analytics
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkAnalyticsItemBlock(
    analytics: ServiceProfilePageState.AnalyticsItem
) {
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
            Text(text = "Завершённых записей")
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
            Text(text = analytics.value)
        },
        overlineText = {
            Text(text = "Популярность среди дугих услуг")
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

@Composable
fun ServiceProfileCompanyAnalyticsItem(
    company: ServiceProfilePageState.CompanyAnalytics,
    modifier: Modifier
) {
    DesignedOutlinedTitledBlock(title = "Компания", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CompanyAnalyticsItemBlock(analytics = company.analytics)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyAnalyticsItemBlock(
    analytics: ServiceProfilePageState.AnalyticsItem
) {
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
            Text(text = "Завершённых записей")
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
            Text(text = analytics.value)
        },
        overlineText = {
            Text(text = "Популярность среди дугих услуг")
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

@Composable
fun ServiceProfileHeaderComponent(
    service: ServiceProfilePageState.Service,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
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
//            network = ServiceProfilePageState.NetworkAnalytics(
//                id = 1,
//                title = "Network-001",
//                analytics = ServiceProfilePageState.AnalyticsItem(
//                    comeCount = 124,
//                    notComeCount = 21,
//                    waitingCount = 8,
//                    totalRecords = 124 + 21 + 8,
//                    value = "66%"
//                ),
//            ),
//            company = ServiceProfilePageState.CompanyAnalytics(
//                id = 1,
//                title = "Company-001",
//                analytics = ServiceProfilePageState.AnalyticsItem(
//                    comeCount = 60,
//                    notComeCount = 12,
//                    waitingCount = 2,
//                    totalRecords = 60 + 12 + 2,
//                    value = "34%"
//                )
//            ),
            isLoading = false,
            isRefreshing = false
        ),
        onEdit = {},
        onRefresh = {},
        modifier = Modifier.fillMaxSize()
    )
}