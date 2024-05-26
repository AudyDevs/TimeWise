package com.example.timewise.ui.activities.home.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.timewise.core.FilterTypes
import com.example.timewise.dispatcher.DispatcherRule
import com.example.timewise.dispatcher.TestDispatchers
import com.example.timewise.domain.usecase.filteredtasks.GetNumberFilteredTasksUseCase
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.label.InsertLabelUseCase
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_NUMBER_OF_FILTERED_TASKS
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfLabelModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getLabelsUseCase: GetLabelsUseCase

    @MockK
    private lateinit var insertLabelsUseCase: InsertLabelUseCase

    @MockK
    private lateinit var getNumberFilteredTasksUseCase: GetNumberFilteredTasksUseCase

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        homeViewModel = HomeViewModel(
            testDispatchers,
            getLabelsUseCase,
            insertLabelsUseCase,
            getNumberFilteredTasksUseCase
        )
    }

    @Test
    fun `when HomeViewModel calls getLabels successfully, it should return the labels`() =
        runBlocking {
            //Given
            coEvery { getLabelsUseCase.invoke() } returns anyListOfLabelModel

            //When
            homeViewModel.getLabels()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val labels = homeViewModel.labels.value

            //Then
            assert(anyListOfLabelModel == labels)
        }

    @Test
    fun `when HomeViewModel calls insertLabel successfully, it should call insertLabelUseCase and getLabelsUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getLabelsUseCase.invoke() } returns anyListOfLabelModel
            coEvery { insertLabelsUseCase.invoke(anyLabelModel) } just runs

            //When
            homeViewModel.insertLabel(anyLabelModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { insertLabelsUseCase.invoke(anyLabelModel) }
            coVerify(exactly = 1) { getLabelsUseCase.invoke() }
        }

    @Test
    fun `when HomeViewModel calls getNumberTasks successfully, it should return the number of filtered tasks for each filter`() =
        runBlocking {
            //Given
            coEvery { getNumberFilteredTasksUseCase.invoke(FilterTypes.Today.type) } returns ANY_NUMBER_OF_FILTERED_TASKS
            coEvery { getNumberFilteredTasksUseCase.invoke(FilterTypes.Week.type) } returns ANY_NUMBER_OF_FILTERED_TASKS
            coEvery { getNumberFilteredTasksUseCase.invoke(FilterTypes.Later.type) } returns ANY_NUMBER_OF_FILTERED_TASKS
            coEvery { getNumberFilteredTasksUseCase.invoke(FilterTypes.Expired.type) } returns ANY_NUMBER_OF_FILTERED_TASKS

            //When
            homeViewModel.getNumberTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val tasksToday = homeViewModel.tasksToday.value
            val tasksWeek = homeViewModel.tasksWeek.value
            val tasksLater = homeViewModel.tasksLater.value
            val tasksExpired = homeViewModel.tasksExpired.value

            //Then
            assert(ANY_NUMBER_OF_FILTERED_TASKS == tasksToday)
            assert(ANY_NUMBER_OF_FILTERED_TASKS == tasksWeek)
            assert(ANY_NUMBER_OF_FILTERED_TASKS == tasksLater)
            assert(ANY_NUMBER_OF_FILTERED_TASKS == tasksExpired)
        }
}