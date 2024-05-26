package com.example.timewise.domain.usecase.label

import com.example.timewise.domain.LabelRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertLabelUseCaseTest {

    @MockK
    private lateinit var repository: LabelRepository
    private lateinit var insertLabelUseCase: InsertLabelUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        insertLabelUseCase = InsertLabelUseCase(repository)
    }

    @Test
    fun `when InsertLabelUseCase is called successfully, LabelRepository should call insertLabel`() =
        runBlocking {
            //Given
            coEvery { repository.insertLabel(anyLabelModel) } just runs

            //When
            insertLabelUseCase.invoke(anyLabelModel)

            //Then
            coVerify(exactly = 1) { repository.insertLabel(anyLabelModel) }
        }
}