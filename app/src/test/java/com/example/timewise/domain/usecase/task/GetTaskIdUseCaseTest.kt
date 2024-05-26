package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTaskIdUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var getTaskIdUseCase: GetTaskIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getTaskIdUseCase = GetTaskIdUseCase(repository)
    }

    @Test
    fun `when GetTaskIdUseCase is called successfully, TaskRepository should return a correct TaskModel`() =
        runBlocking {
            //Given
            coEvery { repository.getTasksId(ANY_ID_TASK) } returns anyTaskModel

            //When
            val taskModel = getTaskIdUseCase.invoke(ANY_ID_TASK)

            //Then
            assert(anyTaskModel == taskModel)
        }
}