package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class StaffProfilePageState(
    val staff: Staff?,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
) {
    data class Staff(
        val name: String,
        val codename: String,
        val role: String
    )
}


@Composable
fun StaffProfilePage(
    state: StaffProfilePageState,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.staff != null)
            ClientProfileHeaderComponent(
                staff = state.staff,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
    }
}

@Composable
fun ClientProfileHeaderComponent(
    staff: StaffProfilePageState.Staff,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //TODO("crop icon")
        Icon(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "иконка",
            modifier = Modifier.size(125.dp)
        )
        Column {
            Text(
                text = staff.name,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = staff.role.ifBlank { staff.codename },
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = staff.codename,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedButton(
                text = "Редактировать".toDesignedString(),
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun StaffProfilePagePreview() {
    StaffProfilePage(
        state = StaffProfilePageState(
            staff = StaffProfilePageState.Staff(
                name = "user user user",
                role = "Менеджер",
                codename = "user_4234234"
            ),
            isLoading = false,
            isRefreshing = false,
        ),
        onEdit = {},
        onRefresh = {},
        modifier = Modifier.fillMaxSize(),
    )
}