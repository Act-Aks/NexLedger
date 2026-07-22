package com.actaks.nexledger.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

internal data class BottomNavItem(
    val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector
)

internal val TOP_LEVEL_DESTINATIONS = mapOf(
    NexLedgerRoute.Dashboard to BottomNavItem(
        "Dashboard",
        Icons.Filled.Home,
        Icons.Outlined.Home
    ),
    NexLedgerRoute.Transactions to BottomNavItem(
        "Transactions",
        Icons.Filled.AddCard,
        Icons.Outlined.AddCard
    ),
    NexLedgerRoute.Accounts to BottomNavItem(
        "Accounts",
        Icons.Filled.AccountBalance,
        Icons.Outlined.AccountBalance
    ),
    NexLedgerRoute.Settings to BottomNavItem(
        "More",
        Icons.Filled.MoreHoriz,
        Icons.Outlined.MoreHoriz
    )
)