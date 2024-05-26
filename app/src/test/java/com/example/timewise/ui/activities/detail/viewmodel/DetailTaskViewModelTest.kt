package com.example.timewise.ui.activities.detail.viewmodel

import com.example.timewise.dispatcher.DispatcherRule
import com.example.timewise.dispatcher.TestDispatchers
import com.example.timewise.domain.usecase.label.GetLabelIdUseCase
import com.example.timewise.domain.usecase.task.DeleteTaskUseCase
import com.example.timewise.domain.usecase.task.GetTaskIdUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskUseCase
import com.example.timewise.motherobject.TimeWiseMotherObject
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
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

class DetailTaskViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @MockK
    private lateinit var getLabelIdUseCase: GetLabelIdUseCase

    @MockK
    private lateinit var getTaskIdUseCase: GetTaskIdUseCase

    @MockK
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @MockK
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @MockK
    private lateinit var updateTaskFinishedUseCase: UpdateTaskFinishedUseCase

    @MockK
    private lateinit var updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase

    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        detailTaskViewModel = DetailTaskViewModel(
            testDispatchers,
            getLabelIdUseCase,
            getTaskIdUseCase,
            updateTaskUseCase,
            deleteTaskUseCase,
            updateTaskFinishedUseCase,
            updateTaskFavouriteUseCase
        )
    }

    @Test
    fun `when DetailTaskViewModel calls getLabelID successfully, it should return a correct LabelModel`() =
        runBlocking {
            //Given
            coEvery { getLabelIdUseCase.invoke(ANY_ID_LABEL) } returns anyLabelModel

            //When
            detailTaskViewModel.getLabelID(ANY_ID_LABEL)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val label = detailTaskViewModel.label.value

            //Then
            assert(label != null)
            assert(anyLabelModel == label)
        }

    @Test
    fun `when DetailTaskViewModel calls getTaskID successfully, it should return a correct TaskModel`() =
        runBlocking {
            //Given
            coEvery { getTaskIdUseCase.invoke(ANY_ID_TASK) } returns anyTaskModel

            //When
            detailTaskViewModel.getTaskID(ANY_ID_TASK)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val task = detailTaskViewModel.task.value

            //Then
            assert(task != null)
            assert(anyTaskModel == task)
        }

    @Test
    fun `when DetailTaskViewModel calls updateTask successfully, it should call updateTaskUseCase and getTaskIdUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTaskIdUseCase.invoke(ANY_ID_TASK) } returns anyTaskModel
            coEvery { updateTaskUseCase.invoke(anyTaskModel) } just runs

            //When
            detailTaskViewModel.updateTask(anyTaskModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { updateTaskUseCase.invoke(anyTaskModel) }
            coVerify(exactly = 1) { getTaskIdUseCase.invoke(ANY_ID_TASK) }
        }

    @Test
    fun `when DetailTaskViewModel calls deleteTask successfully, it should call deleteTaskUseCase once`() =
        runBlocking {
            //Given
            coEvery { deleteTaskUseCase.invoke(anyTaskModel) } just runs

            //When
            detailTaskViewModel.deleteTask(anyTaskModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { deleteTaskUseCase.invoke(anyTaskModel) }
        }


    @Test
    fun `when DetailTaskViewModel calls updateTaskFinished successfully, it should call updateTaskFinishedUseCase and getFilteredTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTaskIdUseCase.invoke(ANY_ID_TASK) } returns anyTaskModel
            coEvery {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
                )
            } just runs

            //When
            detailTaskViewModel.updateTaskFinished(
                ANY_ID_TASK,
                TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
            )
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getTaskIdUseCase.invoke(ANY_ID_TASK) }
        }

    @Test
    fun `when DetailTaskViewModel calls updateTaskFavourite successfully, it should call updateTaskFavouriteUseCase and getFilteredTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTaskIdUseCase.invoke(ANY_ID_TASK) } returns anyTaskModel
            coEvery {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
                )
            } just runs

            //When
            detailTaskViewModel.updateTaskFavourite(
                ANY_ID_TASK,
                TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
            )
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
                )
            }
            coVerify(exactly = 1) { getTaskIdUseCase.invoke(ANY_ID_TASK) }
        }
}