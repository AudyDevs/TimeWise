package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteAllTasksUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var deleteAllTasksUseCase: DeleteAllTasksUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        deleteAllTasksUseCase = DeleteAllTasksUseCase(repository)
    }

    @Test
    fun `when DeleteAllTasksUseCase is called successfully, TaskRepository should call deleteAllTasks`() =
        runBlocking {
            //Given
            coEvery { repository.deleteAllTasks(ANY_ID_LABEL) } just runs

            //When
            deleteAllTasksUseCase.invoke(ANY_ID_LABEL)

            //Then
            coVerify(exactly = 1) { repository.deleteAllTasks(ANY_ID_LABEL) }
        }
}