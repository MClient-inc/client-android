package ru.mclient.ui.servicecategory.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
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

data class ServiceCategoriesListPageState(
    val companies: List<ServiceCategory>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class ServiceCategory(
        val id: Long,
        val title: String,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCategoriesListPage(
    state: ServiceCategoriesListPageState,
    onRefresh: () -> Unit,
    onSelect: (ServiceCategoriesListPageState.ServiceCategory) -> Unit,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    DesignedLazyColumn(
        state = listState,
        refreshing = state.isRefreshing,
        enabled = state.companies.isNotEmpty(),
        loading = state.isLoading,
        empty = state.companies.isEmpty(),
        onRefresh = onRefresh,
        loadingContent = {
            items(6) {
                ServiceCategoryItemPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        modifier = modifier,
    ) {
        items(
            items = state.companies,
            key = ServiceCategoriesListPageState.ServiceCategory::id,
        ) { company ->
            ServiceCategoryItem(
                company = company,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(company) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCategoryItem(
    company: ServiceCategoriesListPageState.ServiceCategory,
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


