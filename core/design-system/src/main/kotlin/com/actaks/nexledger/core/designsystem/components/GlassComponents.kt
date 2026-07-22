package com.actaks.nexledger.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glass-morphism style configuration.
 */
data class GlassStyle(
    val elevation: Dp = 10.dp,
    val alpha: Float = 0.20f,
    val backgroundAlpha: Float = 0.04f,
)

object GlassDefaults {
    val regular = GlassStyle(elevation = 10.dp, alpha = 0.20f, backgroundAlpha = 0.04f)
    val prominent = GlassStyle(elevation = 16.dp, alpha = 0.28f, backgroundAlpha = 0.06f)
    val subtle = GlassStyle(elevation = 4.dp, alpha = 0.12f, backgroundAlpha = 0.02f)
}

/**
 * A frosted-glass card with subtle transparency and elevation — premium feel.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .then(if (onClick != {}) Modifier.clickable(onClick = onClick) else Modifier),
        color = Color.White.copy(alpha = style.alpha),
        tonalElevation = style.elevation,
        shadowElevation = style.elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier.background(Color.White.copy(alpha = style.backgroundAlpha)),
        ) {
            content()
        }
    }
}

/**
 * A frosted-glass surface that doesn't clip — for top bar / overlays.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = style.alpha),
        tonalElevation = style.elevation,
        shadowElevation = style.elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier.background(Color.White.copy(alpha = style.backgroundAlpha)),
        ) {
            content()
        }
    }
}
