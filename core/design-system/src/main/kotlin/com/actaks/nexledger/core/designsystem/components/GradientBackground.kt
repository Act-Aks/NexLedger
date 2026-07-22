package com.actaks.nexledger.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.actaks.nexledger.core.designsystem.theme.GradientBluePrimary
import com.actaks.nexledger.core.designsystem.theme.GradientBlueSecondary
import com.actaks.nexledger.core.designsystem.theme.GradientGreenPrimary
import com.actaks.nexledger.core.designsystem.theme.GradientGreenSecondary
import com.actaks.nexledger.core.designsystem.theme.GradientRedPrimary
import com.actaks.nexledger.core.designsystem.theme.GradientRedSecondary
import com.actaks.nexledger.core.designsystem.theme.slate800
import com.actaks.nexledger.core.designsystem.theme.slate900
import com.actaks.nexledger.core.designsystem.theme.slate950

/**
 * Dark gradient background used behind the Scaffold for a premium, immersive feel.
 *
 * Uses a rich deep-navy-to-black gradient that pairs well with glass-morphism cards.
 */
@Composable
fun GradientBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        slate900,
                        slate800,
                        slate950
                    )
                )
            ),
    )
}

/**
 * Card-level gradient for financial amount highlights.
 */
val IncomeGradient = Brush.horizontalGradient(
    colors = listOf(GradientGreenPrimary, GradientGreenSecondary)
)

val ExpenseGradient = Brush.horizontalGradient(
    colors = listOf(GradientRedPrimary, GradientRedSecondary)
)

val BalanceGradient = Brush.horizontalGradient(
    colors = listOf(GradientBluePrimary, GradientBlueSecondary)
)
