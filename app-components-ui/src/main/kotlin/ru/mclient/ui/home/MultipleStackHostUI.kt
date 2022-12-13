package ru.mclient.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.home.MultipleStackHost

@Composable
fun MultipleStackHostUI(component: MultipleStackHost, modifier: Modifier) {
    Children(stack = component.childStack) {

    }
}

@Composable
fun MultipleStackNavHost() {

}