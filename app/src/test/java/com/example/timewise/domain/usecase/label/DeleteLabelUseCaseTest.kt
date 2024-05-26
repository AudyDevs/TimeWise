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

class DeleteLabelUseCaseTest {

    @MockK
    private lateinit var repository: LabelRepository
    private lateinit var deleteLabelUseCase: DeleteLabelUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        deleteLabelUseCase = DeleteLabelUseCase(repository)
    }

    @Test
    fun `when DeleteLabelUseCase is called successfully, LabelRepository should call deleteLabel`() =
        runBlocking {
            //Given
            coEvery { repository.deleteLabel(anyLabelModel) } just runs

            //When
            deleteLabelUseCase.invoke(anyLabelModel)

            //Then
            coVerify(exactly = 1) { repository.deleteLabel(anyLabelModel) }
        }
}