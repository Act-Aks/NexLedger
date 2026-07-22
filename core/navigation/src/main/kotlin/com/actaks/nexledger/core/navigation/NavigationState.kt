package com.actaks.nexledger.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute by topLevelRoute

    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        configuration = serializersConfig,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(
            configuration = serializersConfig,
            key
        )
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

private val serializersConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(NexLedgerRoute.Accounts::class, NexLedgerRoute.Accounts.serializer())
            subclass(NexLedgerRoute.AddAccount::class, NexLedgerRoute.AddAccount.serializer())
            subclass(NexLedgerRoute.AddBudget::class, NexLedgerRoute.AddBudget.serializer())
            subclass(NexLedgerRoute.AddCategory::class, NexLedgerRoute.AddCategory.serializer())
            subclass(NexLedgerRoute.AddGoal::class, NexLedgerRoute.AddGoal.serializer())
            subclass(
                NexLedgerRoute.AddTransaction::class,
                NexLedgerRoute.AddTransaction.serializer()
            )
            subclass(NexLedgerRoute.Backup::class, NexLedgerRoute.Backup.serializer())
            subclass(NexLedgerRoute.Budgets::class, NexLedgerRoute.Budgets.serializer())
            subclass(NexLedgerRoute.Categories::class, NexLedgerRoute.Categories.serializer())
            subclass(NexLedgerRoute.Dashboard::class, NexLedgerRoute.Dashboard.serializer())
            subclass(NexLedgerRoute.EditAccount::class, NexLedgerRoute.EditAccount.serializer())
            subclass(NexLedgerRoute.EditBudget::class, NexLedgerRoute.EditBudget.serializer())
            subclass(NexLedgerRoute.EditCategory::class, NexLedgerRoute.EditCategory.serializer())
            subclass(NexLedgerRoute.EditGoal::class, NexLedgerRoute.EditGoal.serializer())
            subclass(
                NexLedgerRoute.EditTransaction::class,
                NexLedgerRoute.EditTransaction.serializer()
            )
            subclass(NexLedgerRoute.Goals::class, NexLedgerRoute.Goals.serializer())
            subclass(NexLedgerRoute.Reports::class, NexLedgerRoute.Reports.serializer())
            subclass(NexLedgerRoute.Search::class, NexLedgerRoute.Search.serializer())
            subclass(NexLedgerRoute.Security::class, NexLedgerRoute.Security.serializer())
            subclass(NexLedgerRoute.Settings::class, NexLedgerRoute.Settings.serializer())
            subclass(NexLedgerRoute.Statistics::class, NexLedgerRoute.Statistics.serializer())
            subclass(
                NexLedgerRoute.TransactionDetail::class,
                NexLedgerRoute.TransactionDetail.serializer()
            )
            subclass(NexLedgerRoute.Transactions::class, NexLedgerRoute.Transactions.serializer())
        }
    }
}

@Composable
internal fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse.flatMap { decoratedEntries[it] ?: emptyList() }.toMutableStateList()
}