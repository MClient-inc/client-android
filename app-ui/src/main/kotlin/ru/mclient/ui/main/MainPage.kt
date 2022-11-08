package ru.mclient.ui.main

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.ui.view.CombinedNavigationBarItem
import ru.mclient.ui.view.DesignedDrawable
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedString
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

sealed class MainPageNavBarItem {

    object Home : MainPageNavBarItem()
    object Loyalty : MainPageNavBarItem()
    object Storage : MainPageNavBarItem()
    object Company : MainPageNavBarItem()

}

class NavItem(
    val title: DesignedString,
    val icon: DesignedDrawable,
)

val items by lazy {
    mapOf(
        MainPageNavBarItem.Home to NavItem(
            R.string.home_navitem.toDesignedString(),
            R.drawable.home_navitem.toDesignedDrawable(),
        ),
        MainPageNavBarItem.Loyalty to NavItem(
            R.string.loyalty_navitem.toDesignedString(),
            R.drawable.loyalty_navitem.toDesignedDrawable(),
        ),
        MainPageNavBarItem.Storage to NavItem(
            R.string.storage_navitem.toDesignedString(),
            R.drawable.storage_navitem.toDesignedDrawable(),
        ),
        MainPageNavBarItem.Company to NavItem(
            R.string.company_navitem.toDesignedString(),
            R.drawable.company_navitem.toDesignedDrawable(),
        ),
    )
}

@Composable
fun MainPageNavBar(
    currentItem: MainPageNavBarItem,
    onClick: (MainPageNavBarItem) -> Unit,
    onLongClick: (MainPageNavBarItem) -> Unit,
    modifier: Modifier,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .then(modifier)
    ) {
        items.forEach { (key, value) ->
            CombinedNavigationBarItem(
                selected = currentItem == key,
                onClick = { onClick(key) },
                onLongClick = { onLongClick(key) },
                label = { DesignedText(value.title) },
                icon = { DesignedIcon(value.icon) },
            )
        }
    }
}