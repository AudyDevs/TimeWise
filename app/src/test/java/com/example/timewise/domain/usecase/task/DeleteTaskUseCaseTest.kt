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

class DeleteTaskUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        deleteTaskUseCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `when DeleteTaskUseCase is called successfully, TaskRepository should call deleteTask`() =
        runBlocking {
            //Given
            coEvery { repository.deleteTask(anyTaskModel) } just runs

            //When
            deleteTaskUseCase.invoke(anyTaskModel)

            //Then
            coVerify(exactly = 1) { repository.deleteTask(anyTaskModel) }
        }
}