package ru.mclient.ui.service.profile

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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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


data class ServiceProfilePageState(
    val service: Service?,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) {
    data class Service(
        val title: String,
        val description: String,
        val cost: String
    )
}

@Composable
fun ServiceProfilePage(
    state: ServiceProfilePageState,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (state.service != null)
            ServiceProfileHeaderComponent(
                service = state.service,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
    }
}

@Composable
fun ServiceProfileHeaderComponent(
    service: ServiceProfilePageState.Service,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.service),
            contentDescription = "иконка услуги",
            modifier = Modifier.size(125.dp)
        )
        Column {
            Text(
                text = service.title,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = service.cost,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = service.description,
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
fun ServiceProfilePagePreview() {
    ServiceProfilePage(
        state = ServiceProfilePageState(
            service = ServiceProfilePageState.Service(
                "Стрижка под ноль",
                "Наш лучший мастер - Дядя Толя всегда к вашим услугам",
                "200 рублей"
            ),
            isLoading = false,
            isRefreshing = false
        ),
        onEdit = {},
        onRefresh = {},
        modifier = Modifier.fillMaxSize()
    )
}