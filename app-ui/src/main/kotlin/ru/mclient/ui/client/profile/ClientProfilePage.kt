package ru.mclient.ui.client.profile

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class ClientProfilePageState(
    val profile: Profile?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Profile(
        val name: String,
        val phone: String,
    )
}

@Composable
fun ClientProfilePage(
    state: ClientProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.profile != null)
            ClientProfileHeaderComponent(
                profile = state.profile,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
            )
    }
}

@Composable
fun ClientProfileHeaderComponent(
    profile: ClientProfilePageState.Profile,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .outlined()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.user),
            modifier = Modifier.size(125.dp),
            contentDescription = "иконка"
        )
        Column {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = profile.phone.toPhoneFormat(),
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Редактировать", overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
fun ClientProfilePagePreview() {
    ClientProfilePage(
        state = ClientProfilePageState(
            profile = ClientProfilePageState.Profile(
                name = "Александр Сергеевич Пушкин",
                phone = "78005553535"
            ),
            isLoading = false,
            isRefreshing = false
        ),
        onRefresh = {},
        onEdit = {},
        modifier = Modifier
            .fillMaxSize()
    )
}