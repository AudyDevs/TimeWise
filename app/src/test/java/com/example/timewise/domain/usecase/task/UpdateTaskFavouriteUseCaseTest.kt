package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
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

class UpdateTaskFavouriteUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        updateTaskFavouriteUseCase = UpdateTaskFavouriteUseCase(repository)
    }

    @Test
    fun `when UpdateTaskFavouriteUseCase is called successfully, TaskRepository should call updateTaskFavourite`() =
        runBlocking {
            //Given
            coEvery {
                repository.updateTaskFavourite(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            } just runs

            //When
            updateTaskFavouriteUseCase.invoke(ANY_ID_TASK, ANY_FAVOURITE_STATE_TASK)

            //Then
            coVerify(exactly = 1) {
                repository.updateTaskFavourite(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            }
        }
}