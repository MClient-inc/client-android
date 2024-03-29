package ru.mclient.ui.view.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedDivider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExtendedModalBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: BlurModalBottomSheetState =
        rememberBlurModalBottomSheetState(BlurModalBottomSheetValue.Hidden),
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    showDivider: Boolean = true,
    content: @Composable () -> Unit,
) {
    BlurModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = if (showDivider) {
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    DesignedDivider(modifier = Modifier.fillMaxWidth())
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    sheetContent()
                }
            }
        } else sheetContent,
        modifier = modifier,
        sheetShape = calculateTopShape(offset = sheetState.offset.value)
            .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize),
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        sheetContentColor = sheetContentColor,
        scrimColor = scrimColor,
        content = content,
    )
}

@Composable
private fun calculateTopShape(offset: Float): CornerBasedShape {
    return RoundedCornerShape(calculateShape(min = 0.dp, max = 40.dp, offset = offset * 2.5f))
}

@Composable
private fun calculateShape(min: Dp, max: Dp, offset: Float): CornerSize {
    return remember(offset, min, max) { CornerSize(offset.coerceIn(min.value, max.value)) }
}
