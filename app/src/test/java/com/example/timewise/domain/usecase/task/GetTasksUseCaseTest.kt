package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTasksUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var getTasksUseCase: GetTasksUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getTasksUseCase = GetTasksUseCase(repository)
    }

    @Test
    fun `when GetTasksUseCase is called successfully, TaskRepository should return the tasks`() =
        runBlocking {
            //Given
            coEvery { repository.getTasks(ANY_ID_LABEL) } returns anyListOfTaskModel

            //When
            val tasks = getTasksUseCase.invoke(ANY_ID_LABEL)

            //Then
            assert(anyListOfTaskModel == tasks)
        }
}