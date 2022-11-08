package ru.mclient.ui.client.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import ru.mclient.ui.abonement.clientcreate.NoAbonement
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedTitledBlock
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

data class ClientProfilePageState(
    val profile: Profile?,
    val abonements: List<ClientAbonement>?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Profile(
        val name: String,
        val phone: String,
    )

    class ClientAbonement(
        val id: Long,
        val usages: Int,
        val abonement: Abonement,
    )

    class Abonement(
        val title: String,
        val subabonement: Subabonement,
    )

    class Subabonement(
        val title: String,
        val maxUsages: Int,
    )
}

@Composable
fun ClientProfilePage(
    state: ClientProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onCreateAbonement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.profile != null)
            ClientProfileHeader(
                profile = state.profile,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
            )
        if (state.abonements != null)
            ClientProfileAbonementsList(
                abonements = state.abonements,
                onCreateAbonement = onCreateAbonement,
                modifier = Modifier.fillMaxWidth()
            )
    }
}

@Composable
fun ClientProfileHeader(
    profile: ClientProfilePageState.Profile,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .outlined()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.client),
            modifier = Modifier.size(125.dp),
            contentDescription = "иконка"
        )
        Column {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
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

@Composable
fun ClientProfileAbonementsList(
    abonements: List<ClientProfilePageState.ClientAbonement>,
    onCreateAbonement: () -> Unit,
    modifier: Modifier,
) {
    DesignedTitledBlock(
        title = "Абонементы",
        button = "Добавить",
        onClick = onCreateAbonement,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (abonements.isEmpty())
                NoAbonement(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onCreateAbonement)
                )
            else
                abonements.forEach {
                    ClientProfileAbonementItem(
                        abonement = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileAbonementItem(
    abonement: ClientProfilePageState.ClientAbonement,
    modifier: Modifier,
) {
    ListItem(
        leadingContent = {
            Icon(
                painterResource(id = R.drawable.subabonements),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        headlineText = {
            Text(
                abonement.abonement.title,
            )
        },
        supportingText = {
            Text(abonement.abonement.subabonement.title)
        },
        trailingContent = {
            Text(
                text = "${abonement.usages}/${abonement.abonement.subabonement.maxUsages}",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier.outlined(),
    )
}


@Preview(showBackground = true)
@Composable
fun ClientProfilePagePreview() {
    ClientProfilePage(
        state = ClientProfilePageState(
            profile = ClientProfilePageState.Profile(
                name = "Александр Сергеевич Пушкин",
                phone = "78005553535"
            ),
            abonements = listOf(),
            isLoading = false,
            isRefreshing = false
        ),
        onRefresh = {},
        onEdit = {},
        onCreateAbonement = {},
        modifier = Modifier
            .fillMaxSize()
    )
}