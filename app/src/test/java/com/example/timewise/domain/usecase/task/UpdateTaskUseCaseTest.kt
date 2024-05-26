package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateTaskUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        updateTaskUseCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `when UpdateTaskUseCase is called successfully, TaskRepository should call updateTask`() =
        runBlocking {
            //Given
            coEvery { repository.updateTask(anyTaskModel) } just runs

            //When
            updateTaskUseCase.invoke(anyTaskModel)

            //Then
            coVerify(exactly = 1) { repository.updateTask(anyTaskModel) }
        }
}