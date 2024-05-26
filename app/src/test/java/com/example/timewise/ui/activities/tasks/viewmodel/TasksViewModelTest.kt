package com.example.timewise.ui.activities.tasks.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.timewise.dispatcher.DispatcherRule
import com.example.timewise.dispatcher.TestDispatchers
import com.example.timewise.domain.usecase.label.DeleteLabelUseCase
import com.example.timewise.domain.usecase.label.GetLabelIdUseCase
import com.example.timewise.domain.usecase.label.UpdateLabelUseCase
import com.example.timewise.domain.usecase.task.DeleteAllTasksUseCase
import com.example.timewise.domain.usecase.task.GetTasksUseCase
import com.example.timewise.domain.usecase.task.InsertTaskUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
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
class TasksViewModelTest {

    @get:Rule
    val dispatcherRule = DispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getLabelIdUseCase: GetLabelIdUseCase

    @MockK
    private lateinit var updateLabelUseCase: UpdateLabelUseCase

    @MockK
    private lateinit var deleteLabelUseCase: DeleteLabelUseCase

    @MockK
    private lateinit var getTasksUseCase: GetTasksUseCase

    @MockK
    private lateinit var insertTaskUseCase: InsertTaskUseCase

    @MockK
    private lateinit var updateTaskFinishedUseCase: UpdateTaskFinishedUseCase

    @MockK
    private lateinit var updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase

    @MockK
    private lateinit var deleteAllTasksUseCase: DeleteAllTasksUseCase

    private lateinit var taskViewModel: TasksViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        taskViewModel = TasksViewModel(
            testDispatchers,
            getLabelIdUseCase,
            updateLabelUseCase,
            deleteLabelUseCase,
            getTasksUseCase,
            insertTaskUseCase,
            updateTaskFinishedUseCase,
            updateTaskFavouriteUseCase,
            deleteAllTasksUseCase
        )
    }

    @Test
    fun `when TasksViewModel calls getLabelID successfully, it should return a correct LabelModel`() =
        runBlocking {
            //Given
            coEvery { getLabelIdUseCase.invoke(ANY_ID_LABEL) } returns anyLabelModel

            //When
            taskViewModel.getLabelID(ANY_ID_LABEL)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val label = taskViewModel.label.value

            //Then
            assert(label != null)
            assert(anyLabelModel == label)
        }

    @Test
    fun `when TasksViewModel calls updateLabel successfully, it should call updateLabelUseCase and getLabelIdUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getLabelIdUseCase.invoke(ANY_ID_LABEL) } returns anyLabelModel
            coEvery { updateLabelUseCase.invoke(anyLabelModel) } just runs

            //When
            taskViewModel.updateLabel(anyLabelModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { updateLabelUseCase.invoke(anyLabelModel) }
            coVerify(exactly = 1) { getLabelIdUseCase.invoke(ANY_ID_LABEL) }
        }

    @Test
    fun `when TasksViewModel calls deleteLabel successfully, it should call deleteLabelUseCase and deleteAllTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { deleteLabelUseCase.invoke(anyLabelModel) } just runs
            coEvery { deleteAllTasksUseCase.invoke(ANY_ID_LABEL) } just runs

            //When
            taskViewModel.deleteLabel(anyLabelModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { deleteLabelUseCase.invoke(anyLabelModel) }
            coVerify(exactly = 1) { deleteAllTasksUseCase.invoke(ANY_ID_LABEL) }
        }


    @Test
    fun `when TasksViewModel calls getTasks successfully, it should return the tasks`() =
        runBlocking {
            //Given
            coEvery { getTasksUseCase.invoke(ANY_ID_LABEL) } returns anyListOfTaskModel

            //When
            taskViewModel.idLabel = ANY_ID_LABEL
            taskViewModel.getTasks()
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()
            val tasks = taskViewModel.tasks.value

            //Then
            assert(anyListOfTaskModel == tasks)
        }

    @Test
    fun `when TasksViewModel calls insertTask successfully, it should call insertTaskUseCase and getTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTasksUseCase.invoke(ANY_ID_LABEL) } returns anyListOfTaskModel
            coEvery { insertTaskUseCase.invoke(anyTaskModel) } just runs

            //When
            taskViewModel.idLabel = ANY_ID_LABEL
            taskViewModel.insertTask(anyTaskModel)
            testDispatchers.testDispatchers.scheduler.advanceUntilIdle()

            //Then
            coVerify(exactly = 1) { insertTaskUseCase.invoke(anyTaskModel) }
            coVerify(exactly = 1) { getTasksUseCase.invoke(ANY_ID_LABEL) }
        }

    @Test
    fun `when TasksViewModel calls updateTaskFinished successfully, it should call updateTaskFinishedUseCase and getTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTasksUseCase.invoke(ANY_ID_LABEL) } returns anyListOfTaskModel
            coEvery {
                updateTaskFinishedUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            } just runs

            //When
            taskViewModel.idLabel = ANY_ID_LABEL
            taskViewModel.updateTaskFinished(
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
            coVerify(exactly = 1) { getTasksUseCase.invoke(ANY_ID_LABEL) }
        }

    @Test
    fun `when TasksViewModel calls updateTaskFavourite successfully, it should call updateTaskFavouriteUseCase and getTasksUseCase once each`() =
        runBlocking {
            //Given
            coEvery { getTasksUseCase.invoke(ANY_ID_LABEL) } returns anyListOfTaskModel
            coEvery {
                updateTaskFavouriteUseCase.invoke(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            } just runs

            //When
            taskViewModel.idLabel = ANY_ID_LABEL
            taskViewModel.updateTaskFavourite(
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
            coVerify(exactly = 1) { getTasksUseCase.invoke(ANY_ID_LABEL) }
        }
}