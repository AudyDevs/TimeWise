package com.example.timewise.ui.activities.search.viewmodel

import com.example.timewise.dispatcher.DispatcherRule
import com.example.timewise.dispatcher.TestDispatchers
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.search.GetSearchedTasksUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_SEARCHED_TASKS
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
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

class SearchViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var getSearchedTasksUseCase: GetSearchedTasksUseCase

    @MockK
    private lateinit var getLabelsUseCase: GetLabelsUseCase

    @MockK
    private lateinit var updateTaskFinishedUseCase: UpdateTaskFinishedUseCase

    @MockK
    private lateinit var updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        searchViewModel = SearchViewModel(
            testDispatchers,
            getSearchedTasksUseCase,
            getLabelsUseCase,
            updateTaskFinishedUseCase,
            updateTaskFavouriteUseCase
        )
    }

    @Test
    fun `when SearchViewModel calls getSearchedTasks successfully, it should return the tasks`() =
        runBlocking {
            //Given
            coEvery { getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS) } returns anyListOfTaskModel

            //When
            searchViewModel.search = ANY_SEARCHED_TASKS
            searchViewModel.getSearchedTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val tasks = searchViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == tasks)
        }

    @Test
    fun `when SearchViewModel calls getLabels successfully, it should return the labels`() =
        runBlocking {
            //Given
            coEvery { getLabelsUseCase.invoke() } returns anyListOfLabelModel

            //When
            searchViewModel.getLabels()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val labels = searchViewModel.labels.value

            //Then
            assert(anyListOfLabelModel == labels)
        }

    @Test
    fun `when SearchViewModel calls updateTaskFinished successfully, it should call updateTaskFinishedUseCase and getSearchedTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS) } returns anyListOfTaskModel
            coEvery {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            } just runs

            //When
            searchViewModel.search = ANY_SEARCHED_TASKS
            searchViewModel.updateTaskFinished(
                ANY_ID_TASK,
                ANY_FINISHED_STATE_TASK
            )
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS) }
        }

    @Test
    fun `when SearchViewModel calls updateTaskFavourite successfully, it should call updateTaskFavouriteUseCase and getSearchedTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS) } returns anyListOfTaskModel
            coEvery {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            } just runs

            //When
            searchViewModel.search = ANY_SEARCHED_TASKS
            searchViewModel.updateTaskFavourite(
                ANY_ID_TASK,
                ANY_FAVOURITE_STATE_TASK
            )
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS) }
        }
}