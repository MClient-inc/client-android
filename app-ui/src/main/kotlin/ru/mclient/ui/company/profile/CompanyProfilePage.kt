package ru.mclient.ui.company.profile

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
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedDrawable
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedString
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class CompanyProfilePageState(
    val profile: Profile?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Profile(
        val title: DesignedString,
        val codename: DesignedString,
        val description: DesignedString,
    )
}


@Composable
fun CompanyProfilePage(
    state: CompanyProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onClients: () -> Unit,
    onServices: () -> Unit,
    onStaff: () -> Unit,
    onNetwork: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.profile != null)
            CompanyProfileHeaderComponent(
                profile = state.profile,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
        CompanyProfileBodyComponent(
            onClients = onClients,
            onServices = onServices,
            onStaff = onStaff,
            onNetwork = onNetwork,
            modifier = Modifier
                .fillMaxWidth()
                .outlined()
        )
    }

}

class MenuItem(
    val title: DesignedString,
    val icon: DesignedDrawable? = null,
    val onClick: () -> Unit,
)


@Composable
fun CompanyProfileHeaderComponent(
    profile: CompanyProfilePageState.Profile,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DesignedIcon(
            icon = painterResource(id = R.drawable.company).toDesignedDrawable(),
            modifier = Modifier.size(125.dp)
        )
        Column {
            DesignedText(
                text = profile.title,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            DesignedText(
                text = profile.codename,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedButton(
                text = "Редактировать".toDesignedString(),
                onClick = onEdit,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileBodyComponent(
    onClients: () -> Unit,
    onServices: () -> Unit,
    onStaff: () -> Unit,
    onNetwork: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompanyProfileBodyItems(
        menu = listOf(
            MenuItem(title = "Клиенты".toDesignedString(), onClick = onClients),
            MenuItem(title = "Услуги".toDesignedString(), onClick = onServices),
            MenuItem(title = "Работники".toDesignedString(), onClick = onStaff),
            MenuItem(title = "Сеть".toDesignedString(), onClick = onNetwork),
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileBodyItems(menu: List<MenuItem>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        menu.forEach {
            ListItem(
                headlineText = { DesignedText(text = it.title) },
                leadingContent = {
                    if (it.icon != null) {
                        DesignedIcon(icon = it.icon)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = it.onClick)
            )
        }
    }
}

@Preview
@Composable
fun CompanyProfilePagePreview() {
    CompanyProfilePage(
        state = CompanyProfilePageState(
            profile = CompanyProfilePageState.Profile(
                title = "Компания А".toDesignedString(),
                codename = "mycompany_1234".toDesignedString(),
                description = "это реп для коллег человек куллер говорит буль буль".toDesignedString()
            ),
            isLoading = false,
            isRefreshing = false,
        ),
        onRefresh = {},
        onEdit = {},
        onClients = {},
        onServices = {},
        onStaff = {},
        onNetwork = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}
