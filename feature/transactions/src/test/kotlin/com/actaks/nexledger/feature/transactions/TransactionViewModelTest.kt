package com.actaks.nexledger.feature.transactions

import app.cash.turbine.test
import com.nexledger.core.database.entity.AccountEntity
import com.nexledger.core.database.entity.CategoryEntity
import com.nexledger.core.database.entity.TransactionEntity
import com.nexledger.core.database.repository.AccountRepository
import com.nexledger.core.database.repository.CategoryRepository
import com.nexledger.core.database.repository.TransactionRepository
import com.nexledger.core.model.AccountType
import com.nexledger.core.model.TransactionType
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
class TransactionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val transactionRepo: TransactionRepository = mockk(relaxed = true)
    private val categoryRepo: CategoryRepository = mockk(relaxed = true)
    private val accountRepo: AccountRepository = mockk(relaxed = true)

    private lateinit var viewModel: TransactionViewModel

    private val sampleTx = TransactionEntity(
        id = 1,
        amount = 250.0,
        type = TransactionType.EXPENSE,
        categoryId = 1,
        accountId = 1,
        date = System.currentTimeMillis(),
        note = "Coffee",
        merchant = "Cafe"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { transactionRepo.getAll() } returns flowOf(listOf(sampleTx))
        coEvery { categoryRepo.getAll() } returns flowOf(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Food",
                    type = TransactionType.EXPENSE
                )
            )
        )
        coEvery { accountRepo.getAll() } returns flowOf(
            listOf(
                AccountEntity(
                    id = 1,
                    name = "Wallet",
                    type = AccountType.WALLET,
                    balance = 1000.0
                )
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        viewModel = TransactionViewModel(transactionRepo, categoryRepo, accountRepo)
        viewModel.state.test { assertTrue(awaitItem().loading); cancelAndIgnoreRemainingEvents() }
    }

    @Test
    fun `after refresh, transactions are loaded`() = runTest {
        viewModel = TransactionViewModel(transactionRepo, categoryRepo, accountRepo)
        advanceUntilIdle()
        viewModel.state.test {
            val s = awaitItem(); assertFalse(s.loading); assertEquals(1, s.transactions.size)
            assertEquals("Coffee", s.transactions.first().note); cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search filters transactions`() = runTest {
        viewModel = TransactionViewModel(transactionRepo, categoryRepo, accountRepo)
        advanceUntilIdle()
        viewModel.onAction(TransactionAction.OnSearchChange("Coffee"))
        viewModel.state.test {
            assertEquals(
                "Coffee",
                awaitItem().searchQuery
            ); cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter type updates state`() = runTest {
        viewModel = TransactionViewModel(transactionRepo, categoryRepo, accountRepo)
        advanceUntilIdle()
        viewModel.onAction(TransactionAction.OnFilterTypeSelect(TransactionType.EXPENSE))
        viewModel.state.test {
            assertEquals(
                TransactionType.EXPENSE,
                awaitItem().filterType
            ); cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clear filters resets all filters`() = runTest {
        viewModel = TransactionViewModel(transactionRepo, categoryRepo, accountRepo)
        advanceUntilIdle()
        viewModel.onAction(TransactionAction.OnFilterTypeSelect(TransactionType.EXPENSE))
        advanceUntilIdle()
        viewModel.onAction(TransactionAction.OnClearFilters)
        advanceUntilIdle()
        viewModel.state.test {
            val s = awaitItem(); assertNull(s.filterType); assertNull(s.filterCategoryId)
            assertEquals("", s.searchQuery); cancelAndIgnoreRemainingEvents()
        }
    }
}
