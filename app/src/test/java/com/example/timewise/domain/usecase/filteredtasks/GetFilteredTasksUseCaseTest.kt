package com.example.timewise.domain.usecase.filteredtasks

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTodayFilteredTasks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetFilteredTasksUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var getFilteredTasksUseCase: GetFilteredTasksUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getFilteredTasksUseCase = GetFilteredTasksUseCase(repository)
    }

    @Test
    fun `when GetFilteredTasksUseCase is called successfully, TaskRepository should call getFilteredTasks`() =
        runBlocking {
            //Given
            coEvery { repository.getFilteredTasks(anyTodayFilteredTasks) } returns anyListOfTaskModel

            //When
            getFilteredTasksUseCase.invoke(anyTodayFilteredTasks)

            //Then
            coVerify(exactly = 1) { repository.getFilteredTasks(anyTodayFilteredTasks) }
        }
}