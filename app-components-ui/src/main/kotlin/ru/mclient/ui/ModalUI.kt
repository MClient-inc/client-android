package ru.mclient.ui

import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import ru.mclient.common.Modal
import ru.mclient.common.StackModal
import ru.mclient.ui.view.modal.BlurModalBottomSheetValue
import ru.mclient.ui.view.modal.ExtendedModalBottomSheet
import ru.mclient.ui.view.modal.rememberBlurModalBottomSheetState

@Composable
fun <Child : Any> ModalUI(
    component: Modal,
    stack: ChildStack<*, Child>,
    navHost: @Composable (Child) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalUI(
        component = component,
        sheetContent = {
            Children(stack = stack) {
                navHost(it.instance)
            }
        },
        modifier = modifier,
        content = content,
    )
}


@Composable
fun <Child : Any> ModalUI(
    component: StackModal<Child>,
    navHost: @Composable (Child) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalUI(
        component = component,
        stack = component.modalStack.subscribeAsState().value,
        navHost = navHost,
        modifier = modifier,
        content = content
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalUI(
    component: Modal,
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    content: @Composable () -> Unit,
) {
    val modalState = component.modalState
    val firstState = remember { modalState }
    val sheet =
        rememberBlurModalBottomSheetState(
            initialValue = if (firstState.isVisible) BlurModalBottomSheetValue.Expanded else BlurModalBottomSheetValue.Hidden,
            confirmStateChange = remember(component) {
                {
                    if (it == BlurModalBottomSheetValue.Hidden) {
                        component.updateState(false)
                    }
                    true
                }
            }
        )
    LaunchedEffect(component, modalState, sheet, block = {
        when {
            modalState.isVisible && !sheet.isVisible -> {
                sheet.show()
            }

            !modalState.isVisible && sheet.isVisible -> {
                sheet.hide()
            }

            else -> {
                Log.d(
                    "ModalUI",
                    "State for $component does not affect to sheet state (modalState = ${modalState.isVisible}, sheet = ${sheet.isVisible}/${sheet.currentValue}) "
                )
            }
        }
    })
    ExtendedModalBottomSheet(
        sheetState = sheet,
        sheetContent = sheetContent,
        sheetElevation = 0.dp,
        showDivider = showDivider,
        modifier = modifier,
        content = content,
    )
}