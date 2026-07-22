package com.actaks.nexledger.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.actaks.nexledger.core.designsystem.theme.ShimmerBase
import com.actaks.nexledger.core.designsystem.theme.ShimmerHighlight

/**
 * Shimmer loading animation modifier — draws an animated gradient over the content.
 *
 * Use on placeholder boxes while data is loading or while "load more" is in progress.
 */
fun Modifier.shimmerSkeleton(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        ),
        label = "shimmerAnim",
    )
    drawWithContent {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(ShimmerBase, ShimmerHighlight, ShimmerBase),
                start = Offset(size.width * translateAnim, 0f),
                end = Offset(size.width * (translateAnim + 0.5f), size.height),
            )
        )
    }
}

/** A standalone shimmer card box — ideal for "load more" indicators in lists. */
@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
    widthDp: Int = 160,
    heightDp: Int = 100,
) {
    Box(
        modifier = modifier
            .width(widthDp.dp)
            .height(heightDp.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmerSkeleton(),
    )
}
