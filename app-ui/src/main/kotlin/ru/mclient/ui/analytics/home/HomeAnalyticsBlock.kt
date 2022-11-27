package ru.mclient.ui.analytics.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedOutlinedTitledBlock

data class HomeAnalyticsBlockState(
    val analytics: Analytics?,
) {

    data class Analytics(
        val totalSum: Long,
        val averageSum: Long,
        val comeCount: Int,
        val notComeCount: Int,
        val waitingCount: Int,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAnalyticsBlock(
    state: HomeAnalyticsBlockState,
    modifier: Modifier,
) {
    DesignedOutlinedTitledBlock(title = "Аналитика за день", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.sizeIn(25.dp),
                    )
                },
                headlineText = {
                    Text(
                        text = "${(state.analytics?.averageSum ?: 10000)} ₽",
                        modifier = Modifier.defaultPlaceholder(state.analytics == null),
                    )
                },
                overlineText = {
                    Text("Средний чек")
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.sizeIn(25.dp),
                    )
                },
                headlineText = {
                    Text(
                        text = "${(state.analytics?.totalSum ?: 10000)} ₽",
                        modifier = Modifier.defaultPlaceholder(state.analytics == null),
                    )
                },
                overlineText = {
                    Text("Записей на сумму")
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.sizeIn(25.dp),
                    )
                },
                headlineText = {
                    Text(
                        text = "${(state.analytics?.waitingCount ?: 10000)}",
                        modifier = Modifier.defaultPlaceholder(state.analytics == null),
                    )
                },
                overlineText = {
                    Text("Записей в ожидании")
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.sizeIn(25.dp),
                    )
                },
                headlineText = {
                    Text(
                        text = "${(state.analytics?.comeCount ?: 10000)}",
                        modifier = Modifier.defaultPlaceholder(state.analytics == null),
                    )
                },
                overlineText = {
                    Text("Выполненных записей")
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.sizeIn(25.dp),
                    )
                },
                headlineText = {
                    Text(
                        text = "${(state.analytics?.notComeCount ?: 10000)}",
                        modifier = Modifier.defaultPlaceholder(state.analytics == null),
                    )
                },
                overlineText = {
                    Text("Клиент не пришел")
                }
            )
        }
    }
}