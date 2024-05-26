package com.example.timewise.domain.usecase.search

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_SEARCHED_TASKS
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetSearchedTasksUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var getSearchedTasksUseCase: GetSearchedTasksUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getSearchedTasksUseCase = GetSearchedTasksUseCase(repository)
    }

    @Test
    fun `when GetSearchedTasksUseCase is called successfully, TaskRepository should call getSearchedTasks`() =
        runBlocking {
            //Given
            coEvery { repository.getSearchedTasks(ANY_SEARCHED_TASKS) } returns anyListOfTaskModel

            //When
            getSearchedTasksUseCase.invoke(ANY_SEARCHED_TASKS)

            //Then
            coVerify(exactly = 1) { repository.getSearchedTasks(ANY_SEARCHED_TASKS) }
        }
}