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
        val title: String,
    )

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbonementsListPage(
    state: AbonementsListPageState,
    onClick: (AbonementsListPageState.Abonement) -> Unit,
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
                        onClick = onClick,
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
    onClick: (AbonementsListPageState.Abonement) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .outlined()
            .clickable { onClick(abonement) }
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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun Subabonements(
    subabonements: List<AbonementsListPageState.Subabonement>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        subabonements.forEach {
            Subabonement(subabonement = it)
        }
    }
}

@Composable
private fun Subabonement(
    subabonement: AbonementsListPageState.Subabonement,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.outlined()) {
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
                        AbonementsListPageState.Subabonement("название1"),
                        AbonementsListPageState.Subabonement("название2"),
                        AbonementsListPageState.Subabonement("название3"),
                        AbonementsListPageState.Subabonement("название4"),
                    )
                ),
                AbonementsListPageState.Abonement(
                    2,
                    "Название2",
                    subabonements = listOf(
                        AbonementsListPageState.Subabonement("название1"),
                        AbonementsListPageState.Subabonement("название2"),
                        AbonementsListPageState.Subabonement("название3"),
                        AbonementsListPageState.Subabonement("название4"),
                    )
                ),
                AbonementsListPageState.Abonement(
                    3,
                    "Название",
                    subabonements = listOf(
                        AbonementsListPageState.Subabonement("название1"),
                        AbonementsListPageState.Subabonement("название2"),
                        AbonementsListPageState.Subabonement("название3"),
                        AbonementsListPageState.Subabonement("название4"),
                        AbonementsListPageState.Subabonement("название5"),
                        AbonementsListPageState.Subabonement("название6"),
                        AbonementsListPageState.Subabonement("название7"),
                    )
                ),
            ),
            isLoading = false,
            isRefreshing = false,
            isFailure = false,
        ),
        onClick = {},
        onRefresh = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}