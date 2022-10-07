package ru.mclient.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.combinedSelectable(
    selected: Boolean,
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) = inspectable(inspectorInfo = debugInspectorInfo {
    name = "combinedSelectable"
    properties["selected"] = selected
    properties["interactionSource"] = interactionSource
    properties["indication"] = indication
    properties["enabled"] = enabled
    properties["role"] = role
    properties["onClick"] = onClick
}, factory = {
    Modifier
        .combinedClickable(
            enabled = enabled,
            role = role,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick,
            onLongClick = onLongClick,
        )
        .semantics {
            this.selected = selected
        }
})

@Composable
fun RowScope.CombinedNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
) {
    val styledIcon = @Composable {
        val iconColor by colors.iconColor(selected = selected)
        CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
    }

    val styledLabel: @Composable (() -> Unit)? = label?.let {
        @Composable {
            val style = MaterialTheme.typography.labelSmall
            val textColor by colors.textColor(selected = selected)
            CompositionLocalProvider(LocalContentColor provides textColor) {
                ProvideTextStyle(style, content = label)
            }
        }
    }

    var itemWidth by remember { mutableStateOf(0) }

    Box(
        modifier
            .combinedSelectable(
                selected = selected,
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .weight(1f)
            .onSizeChanged {
                itemWidth = it.width
            }, contentAlignment = Alignment.Center
    ) {
        val animationProgress: Float by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = tween(ItemAnimationDurationMillis)
        )

        // The entire item is selectable, but only the indicator pill shows the ripple. To achieve
        // this, we re-map the coordinates of the item's InteractionSource into the coordinates of
        // the indicator.
        val deltaOffset: Offset
        with(LocalDensity.current) {
            val indicatorWidth = 64.dp.roundToPx()
            deltaOffset = Offset(
                (itemWidth - indicatorWidth).toFloat() / 2, IndicatorVerticalOffset.toPx()
            )
        }
        val offsetInteractionSource = remember(interactionSource, deltaOffset) {
            MappedInteractionSource(interactionSource, deltaOffset)
        }

        // The indicator has a width-expansion animation which interferes with the timing of the
        // ripple, which is why they are separate composables
        val indicatorRipple = @Composable {
            Box(
                Modifier
                    .layoutId(IndicatorRippleLayoutIdTag)
                    .clip(CircleShape)
                    .indication(offsetInteractionSource, rememberRipple())
            )
        }
        val indicator = @Composable {
            Box(
                Modifier
                    .layoutId(IndicatorLayoutIdTag)
                    .background(
                        color = colors.indicatorColor.copy(alpha = animationProgress),
                        shape = CircleShape,
                    )
            )
        }

        NavigationBarItemBaselineLayout(
            indicatorRipple = indicatorRipple,
            indicator = indicator,
            icon = styledIcon,
            label = styledLabel,
            alwaysShowLabel = alwaysShowLabel,
            animationProgress = animationProgress
        )
    }
}

private class MappedInteractionSource(
    underlyingInteractionSource: InteractionSource,
    private val delta: Offset,
) : InteractionSource {
    private val mappedPresses = mutableMapOf<PressInteraction.Press, PressInteraction.Press>()

    override val interactions = underlyingInteractionSource.interactions.map { interaction ->
        when (interaction) {
            is PressInteraction.Press -> {
                val mappedPress = mapPress(interaction)
                mappedPresses[interaction] = mappedPress
                mappedPress
            }

            is PressInteraction.Cancel -> {
                val mappedPress = mappedPresses.remove(interaction.press)
                if (mappedPress == null) {
                    interaction
                } else {
                    PressInteraction.Cancel(mappedPress)
                }
            }

            is PressInteraction.Release -> {
                val mappedPress = mappedPresses.remove(interaction.press)
                if (mappedPress == null) {
                    interaction
                } else {
                    PressInteraction.Release(mappedPress)
                }
            }

            else -> interaction
        }
    }

    private fun mapPress(press: PressInteraction.Press): PressInteraction.Press =
        PressInteraction.Press(press.pressPosition - delta)
}

@Composable
private fun NavigationBarItemBaselineLayout(
    indicatorRipple: @Composable () -> Unit,
    indicator: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
) {
    Layout({
        indicatorRipple()
        if (animationProgress > 0) {
            indicator()
        }

        Box(Modifier.layoutId(IconLayoutIdTag)) { icon() }

        if (label != null) {
            Box(
                Modifier
                    .layoutId(LabelLayoutIdTag)
                    .alpha(if (alwaysShowLabel) 1f else animationProgress)
                    .padding(horizontal = NavigationBarItemHorizontalPadding / 2)
            ) { label() }
        }
    }) { measurables, constraints ->
        val iconPlaceable =
            measurables.first { it.layoutId == IconLayoutIdTag }.measure(constraints)

        val totalIndicatorWidth = iconPlaceable.width + (IndicatorHorizontalPadding * 2).roundToPx()
        val animatedIndicatorWidth = (totalIndicatorWidth * animationProgress).roundToInt()
        val indicatorHeight = iconPlaceable.height + (IndicatorVerticalPadding * 2).roundToPx()
        val indicatorRipplePlaceable =
            measurables.first { it.layoutId == IndicatorRippleLayoutIdTag }.measure(
                Constraints.fixed(
                    width = totalIndicatorWidth, height = indicatorHeight
                )
            )
        val indicatorPlaceable =
            measurables.firstOrNull { it.layoutId == IndicatorLayoutIdTag }?.measure(
                Constraints.fixed(
                    width = animatedIndicatorWidth, height = indicatorHeight
                )
            )

        val labelPlaceable = label?.let {
            measurables.first { it.layoutId == LabelLayoutIdTag }.measure(
                // Measure with loose constraints for height as we don't want the label to
                // take up more space than it needs
                constraints.copy(minHeight = 0)
            )
        }

        if (label == null) {
            placeIcon(iconPlaceable, indicatorRipplePlaceable, indicatorPlaceable, constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                indicatorRipplePlaceable,
                indicatorPlaceable,
                constraints,
                alwaysShowLabel,
                animationProgress
            )
        }
    }
}

/**
 * Places the provided [Placeable]s in the center of the provided [constraints].
 */
private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    indicatorRipplePlaceable: Placeable,
    indicatorPlaceable: Placeable?,
    constraints: Constraints,
): MeasureResult {
    val width = constraints.maxWidth
    val height = constraints.maxHeight

    val iconX = (width - iconPlaceable.width) / 2
    val iconY = (height - iconPlaceable.height) / 2

    val rippleX = (width - indicatorRipplePlaceable.width) / 2
    val rippleY = (height - indicatorRipplePlaceable.height) / 2

    return layout(width, height) {
        indicatorPlaceable?.let {
            val indicatorX = (width - it.width) / 2
            val indicatorY = (height - it.height) / 2
            it.placeRelative(indicatorX, indicatorY)
        }
        iconPlaceable.placeRelative(iconX, iconY)
        indicatorRipplePlaceable.placeRelative(rippleX, rippleY)
    }
}

/**
 * Places the provided [Placeable]s in the correct position, depending on [alwaysShowLabel] and
 * [animationProgress].
 *
 * When [alwaysShowLabel] is true, the positions do not move. The [iconPlaceable] will be placed
 * near the top of the item and the [labelPlaceable] will be placed near the bottom, according to
 * the spec.
 *
 * When [animationProgress] is 1 (representing the selected state), the positions will be the same
 * as above.
 *
 * Otherwise, when [animationProgress] is 0, [iconPlaceable] will be placed in the center, like in
 * [placeIcon], and [labelPlaceable] will not be shown.
 *
 * When [animationProgress] is animating between these values, [iconPlaceable] and [labelPlaceable]
 * will be placed at a corresponding interpolated position.
 *
 * [indicatorRipplePlaceable] and [indicatorPlaceable] will always be placed in such a way that to
 * share the same center as [iconPlaceable].
 *
 * @param labelPlaceable text label placeable inside this item
 * @param iconPlaceable icon placeable inside this item
 * @param indicatorRipplePlaceable indicator ripple placeable inside this item
 * @param indicatorPlaceable indicator placeable inside this item, if it exists
 * @param constraints constraints of the item
 * @param alwaysShowLabel whether to always show the label for this item. If true, icon and label
 * positions will not change. If false, positions transition between 'centered icon with no label'
 * and 'top aligned icon with label'.
 * @param animationProgress progress of the animation, where 0 represents the unselected state of
 * this item and 1 represents the selected state. Values between 0 and 1 interpolate positions of
 * the icon and label.
 */
private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    indicatorRipplePlaceable: Placeable,
    indicatorPlaceable: Placeable?,
    constraints: Constraints,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
): MeasureResult {
    val height = constraints.maxHeight

    val baseline = labelPlaceable[LastBaseline]
    // Label should be `ItemVerticalPadding` from the bottom
    val labelY = height - baseline - NavigationBarItemVerticalPadding.roundToPx()

    // Icon (when selected) should be `ItemVerticalPadding` from the top
    val selectedIconY = NavigationBarItemVerticalPadding.roundToPx()
    val unselectedIconY =
        if (alwaysShowLabel) selectedIconY else (height - iconPlaceable.height) / 2

    // How far the icon needs to move between unselected and selected states.
    val iconDistance = unselectedIconY - selectedIconY

    // The interpolated fraction of iconDistance that all placeables need to move based on
    // animationProgress.
    val offset = (iconDistance * (1 - animationProgress)).roundToInt()

    val containerWidth = constraints.maxWidth

    val labelX = (containerWidth - labelPlaceable.width) / 2
    val iconX = (containerWidth - iconPlaceable.width) / 2

    val rippleX = (containerWidth - indicatorRipplePlaceable.width) / 2
    val rippleY = selectedIconY - IndicatorVerticalPadding.roundToPx()

    return layout(containerWidth, height) {
        indicatorPlaceable?.let {
            val indicatorX = (containerWidth - it.width) / 2
            val indicatorY = selectedIconY - IndicatorVerticalPadding.roundToPx()
            it.placeRelative(indicatorX, indicatorY + offset)
        }
        if (alwaysShowLabel || animationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
        indicatorRipplePlaceable.placeRelative(rippleX, rippleY + offset)
    }
}


private const val IndicatorRippleLayoutIdTag: String = "indicatorRipple"

private const val IndicatorLayoutIdTag: String = "indicator"

private const val IconLayoutIdTag: String = "icon"

private const val LabelLayoutIdTag: String = "label"

private val NavigationBarHeight: Dp = 80.0.dp

private const val ItemAnimationDurationMillis: Int = 100

/*@VisibleForTesting*/
internal val NavigationBarItemHorizontalPadding: Dp = 8.dp

/*@VisibleForTesting*/
internal val NavigationBarItemVerticalPadding: Dp = 16.dp

private val IndicatorHorizontalPadding: Dp = (64.0.dp - 24.0.dp) / 2

private val IndicatorVerticalPadding: Dp = (32.0.dp - 24.0.dp) / 2

private val IndicatorVerticalOffset: Dp = 12.dp


/** Defaults used in [NavigationBarItem]. */
object NavigationBarItemDefaults {

    /**
     * Creates a [NavigationBarItemColors] with the provided colors according to the Material
     * specification.
     *
     * @param selectedIconColor the color to use for the icon when the item is selected.
     * @param selectedTextColor the color to use for the text label when the item is selected.
     * @param indicatorColor the color to use for the indicator when the item is selected.
     * @param unselectedIconColor the color to use for the icon when the item is unselected.
     * @param unselectedTextColor the color to use for the text label when the item is unselected.
     * @return the resulting [NavigationBarItemColors] used for [NavigationBarItem]
     */
    @Composable
    fun colors(
        selectedIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedTextColor: Color = MaterialTheme.colorScheme.onSurface,
        indicatorColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ): NavigationBarItemColors = NavigationBarItemColors(
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        selectedIndicatorColor = indicatorColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
    )
}

@Stable
class NavigationBarItemColors internal constructor(
    private val selectedIconColor: Color,
    private val selectedTextColor: Color,
    private val selectedIndicatorColor: Color,
    private val unselectedIconColor: Color,
    private val unselectedTextColor: Color,
) {
    /**
     * Represents the icon color for this item, depending on whether it is [selected].
     *
     * @param selected whether the item is selected
     */
    @Composable
    internal fun iconColor(selected: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (selected) selectedIconColor else unselectedIconColor,
            animationSpec = tween(ItemAnimationDurationMillis)
        )
    }

    /**
     * Represents the text color for this item, depending on whether it is [selected].
     *
     * @param selected whether the item is selected
     */
    @Composable
    internal fun textColor(selected: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (selected) selectedTextColor else unselectedTextColor,
            animationSpec = tween(ItemAnimationDurationMillis)
        )
    }

    /** Represents the color of the indicator used for selected items. */
    internal val indicatorColor: Color
        get() = selectedIndicatorColor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is NavigationBarItemColors) return false

        if (selectedIconColor != other.selectedIconColor) return false
        if (unselectedIconColor != other.unselectedIconColor) return false
        if (selectedTextColor != other.selectedTextColor) return false
        if (unselectedTextColor != other.unselectedTextColor) return false
        if (selectedIndicatorColor != other.selectedIndicatorColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = selectedIconColor.hashCode()
        result = 31 * result + unselectedIconColor.hashCode()
        result = 31 * result + selectedTextColor.hashCode()
        result = 31 * result + unselectedTextColor.hashCode()
        result = 31 * result + selectedIndicatorColor.hashCode()

        return result
    }
}