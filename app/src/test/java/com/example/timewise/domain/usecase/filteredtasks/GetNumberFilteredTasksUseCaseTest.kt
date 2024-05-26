package com.example.timewise.domain.usecase.filteredtasks

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_NUMBER_OF_FILTERED_TASKS
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTodayFilteredTasks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNumberFilteredTasksUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var getNumberFilteredTasksUseCase: GetNumberFilteredTasksUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getNumberFilteredTasksUseCase = GetNumberFilteredTasksUseCase(repository)
    }

    @Test
    fun `when GetNumberFilteredTasksUseCase is called successfully, TaskRepository should call getNumberFilteredTasks`() =
        runBlocking {
            //Given
            coEvery { repository.getNumberFilteredTasks(anyTodayFilteredTasks) } returns ANY_NUMBER_OF_FILTERED_TASKS

            //When
            getNumberFilteredTasksUseCase.invoke(anyTodayFilteredTasks)

            //Then
            coVerify(exactly = 1) { repository.getNumberFilteredTasks(anyTodayFilteredTasks) }
        }
}