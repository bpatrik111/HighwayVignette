package hu.yettel.highwayvignette.ui

import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConfirmViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: HighwayRepository
    private lateinit var viewModel: ConfirmViewModel

    private val sampleItem = OrderLineItem(type = "DAY", category = "CAR", cost = 5150.0)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = ConfirmViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `confirmOrder transitions to Loading then Success on successful order`() = runTest {
        coEvery { repository.placeOrder(any()) } returns OrderResult.Success(requestId = 1L, receivedOrders = emptyList())

        viewModel.confirmOrder(sampleItem)
        // Before the coroutine has run, the state should already be Loading -
        // this is set synchronously before viewModelScope.launch dispatches.
        assertEquals(ConfirmState.Loading, viewModel.state.value)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(ConfirmState.Success, viewModel.state.value)
    }

    @Test
    fun `confirmOrder transitions to Error on failed order`() = runTest {
        coEvery { repository.placeOrder(any()) } returns OrderResult.Failure("Server error")

        viewModel.confirmOrder(sampleItem)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value is ConfirmState.Error)
        assertEquals("Server error", (viewModel.state.value as ConfirmState.Error).message)
    }

    @Test
    fun `calling confirmOrder again while already loading does not trigger a second network call`() = runTest {
        coEvery { repository.placeOrder(any()) } returns OrderResult.Success(requestId = 1L, receivedOrders = emptyList())

        // First call starts the request and moves to Loading, but the
        // coroutine hasn't completed yet (scheduler not advanced).
        viewModel.confirmOrder(sampleItem)
        // Simulates a rapid double-tap: called again before the first
        // request resolves.
        viewModel.confirmOrder(sampleItem)

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.placeOrder(any()) }
    }
}