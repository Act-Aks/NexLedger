package com.actaks.nexledger.core.database.repository

import app.cash.turbine.test
import com.actaks.nexledger.core.database.dao.TransactionDao
import com.actaks.nexledger.core.database.mapper.toEntity
import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class TransactionRepositoryTest {

    private val dao: TransactionDao = mockk(relaxed = true)
    private lateinit var repository: TransactionRepository

    private val sample = Transaction(
        id = 1,
        amount = 150.0,
        type = TransactionType.EXPENSE,
        categoryId = 2,
        accountId = 3,
        date = 1700000000000L,
        note = "Test",
        merchant = "Test Shop"
    )

    @Before
    fun setUp() {
        repository = TransactionRepositoryImpl(dao)
    }

    @Test
    fun `getAll delegates to DAO`() = runTest {
        coEvery { dao.getAll() } returns flowOf(listOf(sample.toEntity()))

        repository.getAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Test", items.first().note)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insert delegates to DAO and returns id`() = runTest {
        coEvery { dao.insert(sample.toEntity()) } returns 42L

        val id = repository.create(sample)

        assertEquals(42L, id)
        coVerify { dao.insert(sample.toEntity()) }
    }

    @Test
    fun `getById returns entity from DAO`() = runTest {
        coEvery { dao.getById(1) } returns sample.toEntity()

        val result = repository.getById(1)

        assertNotNull(result)
        assertEquals("Test", result!!.note)
    }

    @Test
    fun `deleteById delegates to DAO`() = runTest {
        repository.deleteById(1)

        coVerify { dao.deleteById(1) }
    }

    @Test
    fun `sumByType returns total`() = runTest {
        coEvery { dao.sumByType(TransactionType.EXPENSE) } returns 500.0

        val sum = repository.sumByType(TransactionType.EXPENSE)

        assertEquals(500.0, sum, 0.01)
    }

    @Test
    fun `sumByType returns zero when null`() = runTest {
        coEvery { dao.sumByType(TransactionType.INCOME) } returns null

        val sum = repository.sumByType(TransactionType.INCOME)

        assertEquals(0.0, sum, 0.01)
    }
}
