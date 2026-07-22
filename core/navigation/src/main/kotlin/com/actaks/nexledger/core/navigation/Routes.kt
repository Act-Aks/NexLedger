package com.actaks.nexledger.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe Navigation 3 route definitions.
 *
 * Every route implements [NavKey] so it can be used with [androidx.navigation3.runtime.rememberNavBackStack].
 * Routes with parameters use data classes; parameter-free routes use data objects.
 */
sealed interface NexLedgerRoute : NavKey {

    // ── Tab-level destinations ──

    @Serializable
    data object Dashboard : NexLedgerRoute

    @Serializable
    data object Transactions : NexLedgerRoute

    @Serializable
    data object Accounts : NexLedgerRoute

    @Serializable
    data object Categories : NexLedgerRoute

    @Serializable
    data object Budgets : NexLedgerRoute

    @Serializable
    data object Goals : NexLedgerRoute

    @Serializable
    data object Reports : NexLedgerRoute

    @Serializable
    data object Statistics : NexLedgerRoute

    @Serializable
    data object Search : NexLedgerRoute

    @Serializable
    data object Backup : NexLedgerRoute

    @Serializable
    data object Settings : NexLedgerRoute

    @Serializable
    data object Security : NexLedgerRoute

    // ── Detail / form destinations ──

    @Serializable
    data class AddTransaction(val accountId: Long = 0) : NexLedgerRoute

    @Serializable
    data class EditTransaction(val transactionId: Long) : NexLedgerRoute

    @Serializable
    data class TransactionDetail(val transactionId: Long) : NexLedgerRoute

    @Serializable
    data object AddAccount : NexLedgerRoute

    @Serializable
    data class EditAccount(val accountId: Long) : NexLedgerRoute

    @Serializable
    data object AddCategory : NexLedgerRoute

    @Serializable
    data class EditCategory(val categoryId: Long) : NexLedgerRoute

    @Serializable
    data object AddBudget : NexLedgerRoute

    @Serializable
    data class EditBudget(val budgetId: Long) : NexLedgerRoute

    @Serializable
    data object AddGoal : NexLedgerRoute

    @Serializable
    data class EditGoal(val goalId: Long) : NexLedgerRoute
}

/** Tab destinations that appear in the bottom bar. */
val bottomBarRoutes = setOf<NexLedgerRoute>(
    NexLedgerRoute.Dashboard,
    NexLedgerRoute.Transactions,
    NexLedgerRoute.Accounts,
    NexLedgerRoute.Settings
)

/** All top-level tab destinations. */
val topLevelRoutes = setOf<NexLedgerRoute>(
    NexLedgerRoute.Dashboard,
    NexLedgerRoute.Transactions,
    NexLedgerRoute.Accounts,
    NexLedgerRoute.Categories,
    NexLedgerRoute.Budgets,
    NexLedgerRoute.Goals,
    NexLedgerRoute.Reports,
    NexLedgerRoute.Statistics,
    NexLedgerRoute.Search,
    NexLedgerRoute.Backup,
    NexLedgerRoute.Settings,
    NexLedgerRoute.Security
)
