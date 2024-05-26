package com.example.timewise.domain.usecase.label

import com.example.timewise.domain.LabelRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLabelIdUseCaseTest {

    @MockK
    private lateinit var repository: LabelRepository
    private lateinit var getLabelIdUseCase: GetLabelIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getLabelIdUseCase = GetLabelIdUseCase(repository)
    }

    @Test
    fun `when GetLabelIdUseCase is called successfully, LabelRepository should return a correct LabelModel`() =
        runBlocking {
            //Given
            coEvery { repository.getLabelId(ANY_ID_LABEL) } returns anyLabelModel

            //When
            val labelModel = getLabelIdUseCase.invoke(ANY_ID_LABEL)

            //Then
            assert(anyLabelModel == labelModel)
        }
}