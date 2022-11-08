package ru.mclient.ui.abonement.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

class AbonementsListPageState(
    val abonements: List<Abonement>,
    val isSubabonementClickable: Boolean,
    val isAbonementClickable: Boolean,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val isFailure: Boolean,
) {

    data class Abonement(
        val id: Long,
        val title: String,
        val subabonements: List<Subabonement>,
    )

    data class Subabonement(
        val id: Long,
        val title: String,
    )

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbonementsListPage(
    state: AbonementsListPageState,
    onClickAbonement: (AbonementsListPageState.Abonement) -> Unit,
    onClickSubabonement: (AbonementsListPageState.Abonement, AbonementsListPageState.Subabonement) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier,
) {
    DesignedRefreshColumn(refreshing = state.isFailure, onRefresh = onRefresh) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier,
            content = {
                items(state.abonements, key = AbonementsListPageState.Abonement::id) { record ->
                    AbonementItem(
                        abonement = record,
                        isAbonementClickable = state.isAbonementClickable,
                        isSubabonementClickable = state.isSubabonementClickable,
                        onClick = onClickAbonement,
                        onClickSubabonement = onClickSubabonement,
                        modifier = Modifier
                            .width(180.dp),
                    )
                }
            })
    }
}

@Composable
private fun AbonementItem(
    abonement: AbonementsListPageState.Abonement,
    isAbonementClickable: Boolean,
    isSubabonementClickable: Boolean,
    onClick: (AbonementsListPageState.Abonement) -> Unit,
    onClickSubabonement: (AbonementsListPageState.Abonement, AbonementsListPageState.Subabonement) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .outlined()
            .clickable(onClick = { onClick(abonement) }, enabled = isAbonementClickable)
            .wrapContentSize()
            .padding(10.dp),
    ) {
        Icon(
            painterResource(id = R.drawable.abonement),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(abonement.title, style = MaterialTheme.typography.bodyLarge)
            Subabonements(
                subabonements = abonement.subabonements,
                enabled = isSubabonementClickable,
                onClick = { onClickSubabonement(abonement, it) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun Subabonements(
    subabonements: List<AbonementsListPageState.Subabonement>,
    enabled: Boolean,
    onClick: (AbonementsListPageState.Subabonement) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        subabonements.forEach {
            Subabonement(
                subabonement = it,
                enabled = enabled,
                onClick = onClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun Subabonement(
    subabonement: AbonementsListPageState.Subabonement,
    enabled: Boolean,
    onClick: (AbonementsListPageState.Subabonement) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .outlined()
            .clickable(enabled = enabled, onClick = { onClick(subabonement) })
    ) {
        Text(
            subabonement.title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 7.5.dp),
        )
    }
}

@Preview
@Composable
fun AbonementsListPagePreview() {
    AbonementsListPage(
        state = AbonementsListPageState(
            abonements = listOf(
                AbonementsListPageState.Abonement(
                    1,
                    "Название",
                    subabonements = listOf(
                        AbonementsListPageState.Subabonement(1, "название1"),
                        AbonementsListPageState.Subabonement(2, "название2"),
                        AbonementsListPageState.Subabonement(3, "название3"),
                        AbonementsListPageState.Subabonement(4, "название4"),
                        AbonementsListPageState.Subabonement(5, "название5"),
                        AbonementsListPageState.Subabonement(6, "название6"),
                        AbonementsListPageState.Subabonement(7, "название7"),
                    ),
                ),
                AbonementsListPageState.Abonement(
                    2,
                    "Название2",
                    subabonements = listOf(
                        AbonementsListPageState.Subabonement(1, "название1"),
                        AbonementsListPageState.Subabonement(2, "название2"),
                        AbonementsListPageState.Subabonement(3, "название3"),
                        AbonementsListPageState.Subabonement(4, "название4"),
                        AbonementsListPageState.Subabonement(5, "название5"),
                        AbonementsListPageState.Subabonement(6, "название6"),
                        AbonementsListPageState.Subabonement(7, "название7"),
                    ),
                ),
                AbonementsListPageState.Abonement(
                    3,
                    "Название",
                    subabonements = listOf(
                        AbonementsListPageState.Subabonement(1, "название1"),
                        AbonementsListPageState.Subabonement(2, "название2"),
                        AbonementsListPageState.Subabonement(3, "название3"),
                        AbonementsListPageState.Subabonement(4, "название4"),
                        AbonementsListPageState.Subabonement(5, "название5"),
                        AbonementsListPageState.Subabonement(6, "название6"),
                        AbonementsListPageState.Subabonement(7, "название7"),
                    ),
                ),
            ),
            isSubabonementClickable = false,
            isAbonementClickable = false,
            isLoading = false,
            isRefreshing = false,
            isFailure = false,
        ),
        onClickAbonement = {},
        onClickSubabonement = { _, _ -> },
        onRefresh = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}