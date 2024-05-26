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

class InsertTaskUseCaseTest {

    @MockK
    private lateinit var repository: TaskRepository
    private lateinit var insertTaskUseCase: InsertTaskUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        insertTaskUseCase = InsertTaskUseCase(repository)
    }

    @Test
    fun `when InsertTaskUseCase is called successfully, TaskRepository should call insertTask`() =
        runBlocking {
            //Given
            coEvery { repository.insertTask(anyTaskModel) } just runs

            //When
            insertTaskUseCase.invoke(anyTaskModel)

            //Then
            coVerify(exactly = 1) { repository.insertTask(anyTaskModel) }
        }
}