package ru.mclient.ui.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.mclient.ui.view.CombinedNavigationBarItem
import ru.shafran.ui.R

sealed class MainPageNavBarItem {

    object Home : MainPageNavBarItem()
    object Loyalty : MainPageNavBarItem()
    object Storage : MainPageNavBarItem()
    object Company : MainPageNavBarItem()

}

class NavItem(
    val title: Int,
    val icon: Int,
)

val items by lazy {
    mapOf(
        MainPageNavBarItem.Home to NavItem(R.string.home_navitem, R.drawable.home_navitem),
        MainPageNavBarItem.Loyalty to NavItem(R.string.loyalty_navitem, R.drawable.loyalty_navitem),
        MainPageNavBarItem.Storage to NavItem(R.string.storage_navitem, R.drawable.storage_navitem),
        MainPageNavBarItem.Company to NavItem(R.string.company_navitem, R.drawable.company_navitem),
    )
}

@Composable
fun MainPageNavBar(
    currentItem: MainPageNavBarItem,
    onClick: (MainPageNavBarItem) -> Unit,
    onLongClick: (MainPageNavBarItem) -> Unit,
    modifier: Modifier
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, modifier = modifier) {
        items.forEach {
            CombinedNavigationBarItem(
                selected = currentItem == it.key,
                onClick = { onClick(it.key) },
                onLongClick = { onLongClick(it.key) },
                label = { Text(stringResource(id = it.value.title)) },
                icon = { Icon(painterResource(id = it.value.icon), contentDescription = null) },
            )
        }
    }
}