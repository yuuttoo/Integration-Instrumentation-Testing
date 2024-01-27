package com.ivy.common.androidtest

import app.cash.turbine.test
import com.ivy.core.ui.time.picker.date.DatePickerEvent
import com.ivy.core.ui.time.picker.date.DatePickerViewModel
import com.ivy.core.ui.time.picker.date.data.PickerDay
import com.ivy.core.ui.time.picker.date.data.PickerMonth
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class DatePickerViewModelTest: IvyAndroidTest() {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DatePickerViewModel

    override fun setUp() {
        super.setUp()
        viewModel = DatePickerViewModel(
            appContext = context,
            timeProvider = timeProvider
        )
    }

    @Test
    fun testSelectingDate() = runTest {
        // Making sure, the test runs with a month != February, so
        // February can be selected
        setDate(LocalDate.of(2023, 1, 1))
        viewModel.uiState.test {
            awaitItem() // Skip initial emission

            viewModel.onEvent(DatePickerEvent.DayChange(PickerDay("30", 30)))
            awaitItem() // Skip day emission

            viewModel.onEvent(DatePickerEvent.MonthChange(PickerMonth("Feb", 2)))

            val finalEmission = awaitItem()

            val timeProviderDate = timeProvider.dateNow()
            assertThat(finalEmission.selected).isEqualTo(
                LocalDate.of(timeProviderDate.year, 2, 28)
            )
        }
    }
}