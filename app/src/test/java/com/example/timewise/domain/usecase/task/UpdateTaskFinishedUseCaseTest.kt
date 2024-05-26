package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateTaskFinishedUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var updateTaskFinishedUseCase: UpdateTaskFinishedUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        updateTaskFinishedUseCase = UpdateTaskFinishedUseCase(repository)
    }

    @Test
    fun `when UpdateTaskFinishedUseCase is called successfully, TaskRepository should call updateTaskFinished`() =
        runBlocking {
            //Given
            coEvery {
                repository.updateTaskFinished(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            } just runs

            //When
            updateTaskFinishedUseCase.invoke(ANY_ID_TASK, ANY_FINISHED_STATE_TASK)

            //Then
            coVerify(exactly = 1) {
                repository.updateTaskFinished(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            }
        }
}