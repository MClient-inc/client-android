package ru.mclient.ui.companynetwork.profile

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
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
import ru.mclient.ui.utils.defaultPlaceholder
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

data class CompanyNetworkProfilePageState(
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
fun CompanyNetworkProfilePage(
    state: CompanyNetworkProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onClients: () -> Unit,
    onAnalytics: () -> Unit,
    onServices: () -> Unit,
    onStaff: () -> Unit,
    onCompany: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CompanyNetworkProfileHeader(
            profile = state.profile,
            onEdit = onEdit,
            modifier = Modifier
                .fillMaxWidth()
                .outlined()
                .padding(10.dp),
        )
        CompanyNetworkProfileBody(
            isLoading = state.isLoading  && !state.isRefreshing,
            onClients = onClients,
            onServices = onServices,
            onStaff = onStaff,
            onNetwork = onCompany,
            onAnalytics = onAnalytics,
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
private fun CompanyNetworkProfileHeader(
    profile: CompanyNetworkProfilePageState.Profile?,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (profile != null) {
        CompanyNetworkProfileHeaderComponent(
            profile = profile,
            onEdit = onEdit,
            modifier = modifier
        )
    } else {
        CompanyNetworkProfileHeaderPlaceholder(
            modifier = modifier,
        )
    }
}

@Composable
private fun CompanyNetworkProfileHeaderComponent(
    profile: CompanyNetworkProfilePageState.Profile,
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

@Composable
private fun CompanyNetworkProfileHeaderPlaceholder(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DesignedIcon(
            icon = painterResource(id = R.drawable.company).toDesignedDrawable(),
            modifier = Modifier
                .size(125.dp)
                .defaultPlaceholder()
        )
        Column {
            DesignedText(
                text = "Название компании".toDesignedString(),
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .defaultPlaceholder()
            )
            DesignedText(
                text = "Кодовое имя компании".toDesignedString(),
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedButton(
                text = "Редактировать".toDesignedString(),
                onClick = {},
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultPlaceholder()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyNetworkProfileBody(
    isLoading: Boolean,
    onClients: () -> Unit,
    onServices: () -> Unit,
    onStaff: () -> Unit,
    onNetwork: () -> Unit,
    onAnalytics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        CompanyNetworkProfileBodyPlaceholder(count = 5, modifier = modifier)
    } else {
        CompanyNetworkProfileBodyItems(
            menu = listOf(
                MenuItem(title = "Клиенты".toDesignedString(), onClick = onClients),
                MenuItem(title = "Услуги".toDesignedString(), onClick = onServices),
                MenuItem(title = "Работники".toDesignedString(), onClick = onStaff),
                MenuItem(title = "Компании".toDesignedString(), onClick = onNetwork),
                MenuItem(title = "Аналитика".toDesignedString(), onClick = onAnalytics),
            ),
            modifier = modifier,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyNetworkProfileBodyComponent(
    onClients: () -> Unit,
    onServices: () -> Unit,
    onStaff: () -> Unit,
    onNetwork: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompanyNetworkProfileBodyItems(
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
private fun CompanyNetworkProfileBodyPlaceholder(
    count: Int,
    modifier: Modifier = Modifier,
) {
    CompanyNetworkProfileBodyItemsPlaceholder(
        count = count,
        modifier = modifier,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyNetworkProfileBodyItems(menu: List<MenuItem>, modifier: Modifier = Modifier) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyNetworkProfileBodyItemsPlaceholder(count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        repeat(count) {
            ListItem(
                headlineText = {
                    DesignedText(
                        text = "Пункт меню".toDesignedString(),
                        modifier = Modifier.defaultPlaceholder()
                    )
                },
                leadingContent = {
                    DesignedIcon(
                        icon = Icons.Outlined.Menu.toDesignedDrawable(),
                        modifier = Modifier.defaultPlaceholder()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun CompanyNetworkProfilePagePreview() {
    CompanyNetworkProfilePage(
        state = CompanyNetworkProfilePageState(
            profile = CompanyNetworkProfilePageState.Profile(
                title = "Сеть А".toDesignedString(),
                codename = "mynetwork_1234".toDesignedString(),
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
        onCompany = {},
        onAnalytics = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}
