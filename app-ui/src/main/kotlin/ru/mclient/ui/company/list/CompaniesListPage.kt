package ru.mclient.ui.company.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedLazyColumn
import ru.mclient.ui.view.toDesignedString

data class CompaniesListPageState(
    val companies: List<Company>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Company(
        val id: Long,
        val title: String,
        val codename: String,
        val icon: String?,
    )
}

@Composable
fun CompaniesListPage(
    state: CompaniesListPageState,
    onRefresh: () -> Unit,
    onSelect: (CompaniesListPageState.Company) -> Unit,
    modifier: Modifier,
) {
    DesignedLazyColumn(
        refreshing = state.isRefreshing,
        enabled = state.companies.isNotEmpty(),
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        when {
            state.isLoading && state.companies.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Идёт загрузка...")
                    }
                }
            }

            !state.isLoading && state.companies.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Пусто...")
                        DesignedButton(text = "Обновить".toDesignedString(), onClick = onRefresh)
                    }
                }
            }

            else -> {
                items(
                    items = state.companies,
                    key = CompaniesListPageState.Company::id,
                ) {
                    CompanyItem(
                        company = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(it) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyItem(
    company: CompaniesListPageState.Company,
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


