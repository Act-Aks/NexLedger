package com.actaks.nexledger.feature.dashboard

import app.cash.turbine.test
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.AccountType
import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType
import com.actaks.nexledger.feature.dashboard.presentation.DashboardAction
import com.actaks.nexledger.feature.dashboard.presentation.DashboardViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val transactionRepository: TransactionRepository = mockk(relaxed = true)
    private val budgetRepository: BudgetRepository = mockk(relaxed = true)
    private val categoryRepository: CategoryRepository = mockk(relaxed = true)

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val accounts = listOf(
            Account(id = 1, name = "Checking", type = AccountType.BANK, balance = 5000.0)
        )
        val transactions = listOf(
            Transaction(
                id = 1, amount = 500.0, type = TransactionType.EXPENSE,
                categoryId = 1, accountId = 1, date = System.currentTimeMillis(),
                note = "Groceries", merchant = "Store"
            )
        )
        val budgets = listOf(Budget(id = 1, categoryId = 1, amount = 2000.0))
        val categories = listOf(
            Category(
                id = 1,
                name = "Food",
                type = TransactionType.EXPENSE,
                icon = "restaurant",
                color = "#FF5722"
            )
        )

        coEvery { accountRepository.getAll() } returns flowOf(accounts)
        coEvery { accountRepository.getTotalBalance() } returns 5000.0
        coEvery { transactionRepository.getRecent(any()) } returns flowOf(transactions)
        coEvery { transactionRepository.sumByTypeAndDateRange(any(), any(), any()) } returns 1500.0
        coEvery { budgetRepository.getAll() } returns flowOf(budgets)
        coEvery { categoryRepository.getAll() } returns flowOf(categories)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel = DashboardViewModel(
            accountRepository,
            budgetRepository,
            categoryRepository,
            transactionRepository
        )
        viewModel.state.test {
            assertTrue(awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `after refresh, state contains accounts and transactions`() = runTest {
        viewModel = DashboardViewModel(
            accountRepository,
            budgetRepository,
            categoryRepository,
            transactionRepository
        )
        advanceUntilIdle()
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.loading)
            assertEquals(5000.0, state.totalBalance, 0.01)
            assertEquals(1, state.recentTransactions.size)
            assertEquals("Groceries", state.recentTransactions.first().note)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `errorDismissed clears error`() = runTest {
        viewModel = DashboardViewModel(
            accountRepository,
            budgetRepository,
            categoryRepository,
            transactionRepository
        )
        advanceUntilIdle()
        viewModel.onAction(DashboardAction.OnErrorDismissed)
        viewModel.state.test {
            assertNull(awaitItem().error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
