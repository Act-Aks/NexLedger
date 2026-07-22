package com.actaks.nexledger.core.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.actaks.nexledger.feature.accounts.AccountsRoot
import com.actaks.nexledger.feature.accounts.form.AccountFormRoot
import com.actaks.nexledger.feature.backup.presentation.BackupRoot
import com.actaks.nexledger.feature.budgets.presentation.BudgetRoot
import com.actaks.nexledger.feature.budgets.presentation.form.BudgetFormRoot
import com.actaks.nexledger.feature.categories.presentation.CategoryRoot
import com.actaks.nexledger.feature.categories.presentation.form.CategoryFormRoot
import com.actaks.nexledger.feature.dashboard.presentation.DashboardRoot
import com.actaks.nexledger.feature.goals.presentation.GoalsRoot
import com.actaks.nexledger.feature.goals.presentation.form.GoalFormRoot
import com.actaks.nexledger.feature.reports.ReportsRoot
import com.actaks.nexledger.feature.search.SearchRoot
import com.actaks.nexledger.feature.security.SecurityRoot
import com.actaks.nexledger.feature.settings.SettingsRoot
import com.actaks.nexledger.feature.statistics.StatisticsRoot
import com.actaks.nexledger.feature.transactions.TransactionsRoot
import com.actaks.nexledger.feature.transactions.detail.TransactionDetailRoot
import com.actaks.nexledger.feature.transactions.form.TransactionFormRoot

@Composable
fun NexledgerNavigationRoot(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = NexLedgerRoute.Dashboard,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember { Navigator(navigationState) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NexLedgerNavigationBar(
                selectedKey = navigationState.topLevelRoute,
                onSelectKey = { navigator.navigate(it) }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBack = navigator::goBack,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<NexLedgerRoute.Accounts> {
                        AccountsRoot(
                            onNavigateToAddAccount = {
                                navigator.navigate(NexLedgerRoute.AddAccount)
                            },
                            onNavigateToEditAccount = {
                                navigator.navigate(NexLedgerRoute.EditAccount(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.AddAccount> {
                        AccountFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.AddBudget> {
                        BudgetFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.AddCategory> {
                        CategoryFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.AddGoal> {
                        GoalFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.AddTransaction> {
                        TransactionFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.Backup> {
                        BackupRoot()
                    }
                    entry<NexLedgerRoute.Budgets> {
                        BudgetRoot(
                            onNavigateToAddBudget = {
                                navigator.navigate(NexLedgerRoute.AddBudget)
                            },
                            onNavigateToEditBudget = {
                                navigator.navigate(NexLedgerRoute.EditBudget(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.Categories> {
                        CategoryRoot(
                            onNavigateToAddCategory = {
                                navigator.navigate(NexLedgerRoute.AddCategory)
                            },
                            onNavigateToEditCategory = {
                                navigator.navigate(NexLedgerRoute.EditCategory(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.Dashboard> {
                        DashboardRoot(
                            onNavigateToTransactions = {
                                navigator.navigate(NexLedgerRoute.Transactions)
                            },
                            onNavigateToAccounts = {
                                navigator.navigate(NexLedgerRoute.Accounts)
                            },
                            onNavigateToBudgets = {
                                navigator.navigate(NexLedgerRoute.Budgets)
                            },
                            onNavigateToAddTransaction = {
                                navigator.navigate(NexLedgerRoute.AddTransaction())
                            },
                            onNavigateToTransactionDetail = {
                                navigator.navigate(NexLedgerRoute.TransactionDetail(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.EditAccount> {
                        AccountFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.EditBudget> {
                        BudgetFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.EditCategory> {
                        CategoryFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.EditGoal> {
                        GoalFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.EditTransaction> {
                        TransactionFormRoot(
                            onNavigateBack = navigator::goBack,
                        )
                    }
                    entry<NexLedgerRoute.Goals> {
                        GoalsRoot(
                            onNavigateToAddGoal = {
                                navigator.navigate(NexLedgerRoute.AddGoal)
                            },
                            onNavigateToEditGoal = {
                                navigator.navigate(NexLedgerRoute.EditGoal(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.Reports> {
                        ReportsRoot()
                    }
                    entry<NexLedgerRoute.Search> {
                        SearchRoot(
                            onNavigateToTransactionDetail = {
                                navigator.navigate(NexLedgerRoute.TransactionDetail(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.Security> {
                        SecurityRoot()
                    }
                    entry<NexLedgerRoute.Settings> {
                        SettingsRoot(
                            onNavigateToSecurity = {
                                navigator.navigate(NexLedgerRoute.Security)
                            },
                            onNavigateToBackup = {
                                navigator.navigate(NexLedgerRoute.Backup)
                            },
                        )
                    }
                    entry<NexLedgerRoute.Statistics> {
                        StatisticsRoot()
                    }
                    entry<NexLedgerRoute.TransactionDetail> {
                        TransactionDetailRoot(
                            onNavigateBack = navigator::goBack,
                            onNavigateToEdit = {
                                navigator.navigate(NexLedgerRoute.EditTransaction(it))
                            },
                        )
                    }
                    entry<NexLedgerRoute.Transactions> {
                        TransactionsRoot(
                            onNavigateToAddTransaction = {
                                navigator.navigate(NexLedgerRoute.AddTransaction())
                            },
                            onNavigateToEditTransaction = {
                                navigator.navigate(NexLedgerRoute.EditTransaction(it))
                            },
                            onNavigateToTransactionDetail = {
                                navigator.navigate(NexLedgerRoute.TransactionDetail(it))
                            },
                        )
                    }
                }
            )
        )
    }
}