package ru.mclient.ui.client.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedLazyColumn
import ru.mclient.ui.view.DesignedText
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
        val name: String,
        val phone: String,
    )
}

@Composable
fun ClientsListPage(
    state: ClientListPageState,
    onRefresh: () -> Unit,
    onSelect: (ClientListPageState.Client) -> Unit,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    DesignedLazyColumn(
        state = listState,
        refreshing = state.isRefreshing,
        enabled = state.clients.isNotEmpty(),
        loading = state.isLoading,
        empty = state.clients.isEmpty(),
        onRefresh = onRefresh,
        modifier = modifier,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientListItem(
    client: ClientListPageState.Client,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            if (client.name.isNotBlank())
                Text(client.name)
        },
        supportingText = {
            if (client.phone.isNotBlank())
                Text(client.phone)
        },
        modifier = modifier,
        leadingContent = {
            Icon(painterResource(id = R.drawable.client), contentDescription = null, modifier = Modifier.size(35.dp))
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


