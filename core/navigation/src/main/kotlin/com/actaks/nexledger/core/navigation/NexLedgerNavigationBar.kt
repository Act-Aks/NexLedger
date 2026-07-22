package com.actaks.nexledger.core.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey

@Composable
fun NexLedgerNavigationBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { (topLevelDestination, data) ->
            val selected = topLevelDestination == selectedKey
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectKey(topLevelDestination) },
                icon = {
                    Icon(
                        imageVector = if (selected) data.selectedIcon else data.unselectedIcon,
                        contentDescription = data.label
                    )
                },
                label = {
                    Text(data.label)
                }
            )
        }
    }
}