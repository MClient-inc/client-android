package ru.mclient.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.router.stack.ChildStack
import ru.mclient.common.main.MainHost
import ru.mclient.ui.company.CompanyUI
import ru.mclient.ui.home.HomeUI
import ru.mclient.ui.loyalty.LoyaltyUI
import ru.mclient.ui.storage.StorageUI


@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
fun MainUI(component: MainHost, modifier: Modifier) {
    val childStack = component.childStack
    Scaffold(
        bottomBar = {
            MainPageNavBar(
                currentItem = childStack.currentNavItem(),
                onClick = {
                    when (it) {
                        MainPageNavBarItem.Company -> component.onCompany()
                        MainPageNavBarItem.Home -> component.onHost()
                        MainPageNavBarItem.Loyalty -> component.onLoyalty()
                        MainPageNavBarItem.Storage -> component.onStorage()
                    }
                },
                onLongClick = {},
                modifier = Modifier
                    .padding(10.dp)
                    .border(
                        Dp.Hairline,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
            )
        },
        modifier = modifier,
    ) { paddingValue ->
        Children(childStack) {
            MainNavHost(
                child = it.instance,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
            )
        }

    }
}


@Composable
fun ChildStack<*, MainHost.Child>.currentNavItem(): MainPageNavBarItem {
    return when (active.instance) {
        is MainHost.Child.Company -> MainPageNavBarItem.Company
        is MainHost.Child.Home -> MainPageNavBarItem.Home
        is MainHost.Child.Loyalty -> MainPageNavBarItem.Loyalty
        is MainHost.Child.Storage -> MainPageNavBarItem.Storage
    }
}

@Composable
fun MainNavHost(child: MainHost.Child, modifier: Modifier) {
    when (child) {
        is MainHost.Child.Company ->
            CompanyUI(component = child.component, modifier = modifier)

        is MainHost.Child.Home ->
            HomeUI(component = child.component, modifier = modifier)

        is MainHost.Child.Loyalty ->
            LoyaltyUI(component = child.component, modifier = modifier)

        is MainHost.Child.Storage ->
            StorageUI(component = child.component, modifier = modifier)
    }
}