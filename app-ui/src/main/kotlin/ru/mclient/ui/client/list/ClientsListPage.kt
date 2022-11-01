package ru.mclient.ui.client.list

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
import androidx.compose.material.icons.outlined.Person
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

data class ClientListPageState(
    val clients: List<Client>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Client(
        val id: Long,
        val title: String,
        val phone: String,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsListPage(
    state: ClientListPageState,
    onRefresh: () -> Unit,
    onSelect: (ClientListPageState.Client) -> Unit,
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
            enabled = state.clients.isNotEmpty(),
            loading = state.isLoading,
            empty = state.clients.isEmpty(),
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            loadingContent = {
                items(6) {
                    ClientListItemPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            },
        ) {
            items(
                items = state.clients,
                key = ClientListPageState.Client::id,
            ) { client ->
                ClientListItem(
                    client = client,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(client) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientListItem(
    client: ClientListPageState.Client,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            if (client.title.isBlank())
                Text(client.phone)
            else
                Text(client.title)
        },
        supportingText = {
            if (client.phone.isBlank())
                Text(client.title)
            else
                Text(client.phone)
        },
        modifier = modifier,
        leadingContent = {
            Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(35.dp))
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientListItemPlaceholder(
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

