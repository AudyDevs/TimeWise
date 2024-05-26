package com.example.timewise.ui.activities.filtered.viewmodel

import com.example.timewise.dispatcher.DispatcherRule
import com.example.timewise.dispatcher.TestDispatchers
import com.example.timewise.domain.usecase.filteredtasks.GetFilteredTasksUseCase
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.anyExpiredFilteredTasks
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLaterFilteredTasks
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTodayFilteredTasks
import com.example.timewise.motherobject.TimeWiseMotherObject.anyWeekFilteredTasks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilteredTasksViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var getFilteredTasksUseCase: GetFilteredTasksUseCase

    @MockK
    private lateinit var getLabelsUseCase: GetLabelsUseCase

    @MockK
    private lateinit var updateTaskFinishedUseCase: UpdateTaskFinishedUseCase

    @MockK
    private lateinit var updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase

    private lateinit var filteredTasksViewModel: FilteredTasksViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        filteredTasksViewModel = FilteredTasksViewModel(
            testDispatchers,
            getFilteredTasksUseCase,
            getLabelsUseCase,
            updateTaskFinishedUseCase,
            updateTaskFavouriteUseCase
        )
    }

    @Test
    fun `when FilteredTasksViewModel calls getFilteredTasks with today filter successfully, it should return the today tasks`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyTodayFilteredTasks) } returns anyListOfTaskModel
            filteredTasksViewModel.filterTypes = anyTodayFilteredTasks

            //When
            filteredTasksViewModel.getFilteredTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val todayTasks = filteredTasksViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == todayTasks)
        }

    @Test
    fun `when FilteredTasksViewModel calls getFilteredTasks with week filter successfully, it should return the week tasks`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyWeekFilteredTasks) } returns anyListOfTaskModel
            filteredTasksViewModel.filterTypes = anyWeekFilteredTasks

            //When
            filteredTasksViewModel.getFilteredTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val weekTasks = filteredTasksViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == weekTasks)
        }

    @Test
    fun `when FilteredTasksViewModel calls getFilteredTasks with later filter successfully, it should return the later tasks`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyLaterFilteredTasks) } returns anyListOfTaskModel
            filteredTasksViewModel.filterTypes = anyLaterFilteredTasks

            //When
            filteredTasksViewModel.getFilteredTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val laterTasks = filteredTasksViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == laterTasks)
        }

    @Test
    fun `when FilteredTasksViewModel calls getFilteredTasks with expired filter successfully, it should return the expired tasks`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyExpiredFilteredTasks) } returns anyListOfTaskModel
            filteredTasksViewModel.filterTypes = anyExpiredFilteredTasks

            //When
            filteredTasksViewModel.getFilteredTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val expiredTasks = filteredTasksViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == expiredTasks)
        }

    @Test
    fun `when FilteredTasksViewModel calls getLabels successfully, it should return the labels`() =
        runBlocking {
            //Given
            coEvery { getLabelsUseCase.invoke() } returns anyListOfLabelModel

            //When
            filteredTasksViewModel.getLabels()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val labels = filteredTasksViewModel.labels.value

            //Then
            assert(anyListOfLabelModel == labels)
        }

    @Test
    fun `when FilteredTasksViewModel calls updateTaskFinished successfully, it should call updateTaskFinishedUseCase and getFilteredTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyTodayFilteredTasks) } returns anyListOfTaskModel
            coEvery {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            } just runs

            //When
            filteredTasksViewModel.updateTaskFinished(ANY_ID_TASK, ANY_FINISHED_STATE_TASK)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getFilteredTasksUseCase.invoke(anyTodayFilteredTasks) }
        }

    @Test
    fun `when FilteredTasksViewModel calls updateTaskFavourite successfully, it should call updateTaskFavouriteUseCase and getFilteredTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getFilteredTasksUseCase.invoke(anyTodayFilteredTasks) } returns anyListOfTaskModel
            coEvery {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            } just runs

            //When
            filteredTasksViewModel.updateTaskFavourite(ANY_ID_TASK, ANY_FAVOURITE_STATE_TASK)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getFilteredTasksUseCase.invoke(anyTodayFilteredTasks) }
        }
}