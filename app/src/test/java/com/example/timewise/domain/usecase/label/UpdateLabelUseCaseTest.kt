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

class UpdateLabelUseCaseTest {

    @MockK
    private lateinit var repository: LabelRepository
    private lateinit var updateLabelUseCase: UpdateLabelUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        updateLabelUseCase = UpdateLabelUseCase(repository)
    }

    @Test
    fun `when UpdateLabelUseCase is called successfully, LabelRepository should call updateLabel`() =
        runBlocking {
            //Given
            coEvery { repository.updateLabel(anyLabelModel) } just runs

            //When
            updateLabelUseCase.invoke(anyLabelModel)

            //Then
            coVerify(exactly = 1) { repository.updateLabel(anyLabelModel) }
        }
}