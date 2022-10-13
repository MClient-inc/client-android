package ru.mclient.ui.companynetwork.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedLazyColumn
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class CompanyNetworksListPageState(
    val companies: List<CompanyNetwork>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

@Composable
fun CompanyNetworksListPage(
    state: CompanyNetworksListPageState,
    onRefresh: () -> Unit,
    onSelect: (CompanyNetworksListPageState.CompanyNetwork) -> Unit,
    modifier: Modifier,
) {
    DesignedLazyColumn(
        refreshing = state.isRefreshing,
        enabled = state.companies.isNotEmpty(),
        loading = state.isLoading,
        empty = state.companies.isEmpty(),
        onRefresh = onRefresh,
        modifier = modifier,
        loadingContent = {
            items(6) {
                CompanyNetworkItemPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
    ) {
        items(
            items = state.companies,
            key = CompanyNetworksListPageState.CompanyNetwork::id,
        ) {
            CompanyNetworkItem(
                company = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(it) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNetworkItem(
    company: CompanyNetworksListPageState.CompanyNetwork,
    modifier: Modifier,
) {
    ListItem(
        headlineText = { Text(company.title) },
        supportingText = { Text(company.codename) },
        modifier = modifier,
        leadingContent = {
            Icon(Icons.Outlined.Menu, contentDescription = null, modifier = Modifier.size(35.dp))
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNetworkItemPlaceholder(
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


