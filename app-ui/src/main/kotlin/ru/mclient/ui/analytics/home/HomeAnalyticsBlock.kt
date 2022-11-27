package ru.mclient.ui.analytics.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedOutlinedTitledBlock

data class HomeAnalyticsBlockState(
    val analytics: Analytics?,
) {


    class AnalyticItem(
        val value: String,
        val difference: Int,
    )

    class Analytics(
        val totalSum: AnalyticItem,
        val averageSum: AnalyticItem,
        val comeCount: AnalyticItem,
        val notComeCount: AnalyticItem,
        val waitingCount: AnalyticItem,
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
            AnalyticItemBlock(
                title = "Средний чек",
                item = state.analytics?.averageSum,
            )
            AnalyticItemBlock(
                title = "Записей на сумму",
                item = state.analytics?.totalSum,
            )
            AnalyticItemBlock(
                title = "Записей в ожидании",
                item = state.analytics?.waitingCount,
            )
            AnalyticItemBlock(
                title = "Выполненных записей",
                item = state.analytics?.comeCount,
            )
            AnalyticItemBlock(
                title = "Клиент не пришёл",
                item = state.analytics?.notComeCount,
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyticItemBlock(
    title: String,
    item: HomeAnalyticsBlockState.AnalyticItem?,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            when {
                item == null ->
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier
                            .sizeIn(25.dp)
                            .defaultPlaceholder(),
                    )

                item.difference >= 0 ->
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.sizeIn(25.dp),
                    )

                else ->
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.sizeIn(25.dp),
                    )
            }
        },
        headlineText = {
            Text(
                text = item?.value ?: "значение",
                modifier = Modifier.defaultPlaceholder(item == null),
            )
        },
        supportingText = {
            Text(
                "на ${item?.difference}%",
                modifier = Modifier.defaultPlaceholder(item == null),
            )
        },
        overlineText = {
            Text(title)
        }, modifier = modifier
    )
}