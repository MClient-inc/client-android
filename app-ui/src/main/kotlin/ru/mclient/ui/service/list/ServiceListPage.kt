package ru.mclient.ui.service.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedLazyColumn
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.ExtendOnShowFloatingActionButton
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class ServicesListPageState(
    val services: List<Service>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Service(
        val id: Long,
        val title: String,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesListPage(
    state: ServicesListPageState,
    onRefresh: () -> Unit,
    onSelect: (ServicesListPageState.Service) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    Scaffold(
        floatingActionButton = {
            ExtendOnShowFloatingActionButton(
                text = "Добавить".toDesignedString(),
                icon = Icons.Outlined.Add.toDesignedDrawable(),
                isShown = !state.isLoading || state.isRefreshing,
                onClick = onCreate,
                isScrollInProgress = listState.isScrollInProgress,
            )
        },
        modifier = modifier,
    ) {
        DesignedLazyColumn(
            state = listState,
            refreshing = state.isRefreshing,
            enabled = state.services.isNotEmpty(),
            loading = state.isLoading,
            empty = state.services.isEmpty(),
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            loadingContent = {
                items(6) {
                    ServiceCategoryItemPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            },
        ) {
            items(
                items = state.services,
                key = ServicesListPageState.Service::id,
            ) { service ->
                ServiceCategoryItem(
                    company = service,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(service) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCategoryItem(
    company: ServicesListPageState.Service,
    modifier: Modifier,
) {
    ListItem(
        headlineText = { Text(company.title) },
        modifier = modifier,
        leadingContent = {
            Icon(Icons.Outlined.Menu, contentDescription = null, modifier = Modifier.size(35.dp))
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCategoryItemPlaceholder(
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            DesignedText(
                "Название сети".toDesignedString(),
                modifier = Modifier.defaultPlaceholder()
            )
        },
        supportingText = {
            DesignedText(
                "Кодовое имя".toDesignedString(),
                modifier = Modifier.defaultPlaceholder()
            )
        },
        modifier = modifier,
        leadingContent = {
            DesignedIcon(
                icon = R.drawable.company.toDesignedDrawable(),
                modifier = Modifier
                    .size(35.dp)
                    .defaultPlaceholder()
            )
        },
    )
}


