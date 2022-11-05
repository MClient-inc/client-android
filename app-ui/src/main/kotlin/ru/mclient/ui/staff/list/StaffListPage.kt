package ru.mclient.ui.staff.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.DesignedDrawable
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedLazyColumn
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class StaffListPageState(
    val staff: List<Staff>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
        val icon: DesignedDrawable?,
    )
}

@Composable
fun StaffListPage(
    state: StaffListPageState,
    onRefresh: () -> Unit,
    onSelect: (StaffListPageState.Staff) -> Unit,
    modifier: Modifier,
) {
    val listState = rememberLazyListState()
    DesignedLazyColumn(
        refreshing = state.isRefreshing,
        enabled = state.staff.isNotEmpty(),
        onRefresh = onRefresh,
        loading = state.isLoading,
        state = listState,
        empty = state.staff.isEmpty(),
        modifier = modifier,
        loadingContent = {
            items(6) {
                StaffItemPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
    ) {
        items(state.staff, key = StaffListPageState.Staff::id) { staff ->
            StaffItem(staff = staff,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(staff) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffItem(
    staff: StaffListPageState.Staff,
    modifier: Modifier,
) {
    ListItem(
        headlineText = { Text(staff.name) },
        supportingText = {
            if (staff.role.isNotBlank())
                Text(staff.role)
        },
        modifier = modifier,
        leadingContent = {
            DesignedIcon(
                icon = staff.icon ?: Icons.Outlined.Menu.toDesignedDrawable(),
                modifier = Modifier.size(35.dp)
            )
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffItemPlaceholder(
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            DesignedText(
                "Имя работника".toDesignedString(),
                modifier = Modifier.defaultPlaceholder()
            )
        },
        supportingText = {
            DesignedText(
                "Роль работника".toDesignedString(),
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


@Composable
@Preview
fun StaffListPagePreview() {
    StaffListPage(
        state = StaffListPageState(
            staff = List(6) {
                StaffListPageState.Staff(
                    it.toLong(),
                    "name",
                    "codename",
                    "role",
                    null
                )
            },
            isLoading = false,
            isRefreshing = false,
        ),
        onRefresh = {},
        onSelect = {},
        modifier = Modifier.fillMaxSize(),
    )
}