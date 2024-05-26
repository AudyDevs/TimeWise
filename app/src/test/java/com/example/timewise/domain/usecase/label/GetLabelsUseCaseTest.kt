package com.example.timewise.domain.usecase.label

import com.example.timewise.domain.LabelRepository
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfLabelModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLabelsUseCaseTest {

    @MockK
    private lateinit var repository: LabelRepository
    private lateinit var getLabelsUseCase: GetLabelsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getLabelsUseCase = GetLabelsUseCase(repository)
    }

    @Test
    fun `when GetLabelsUseCase is called successfully, LabelRepository should return the labels`() =
        runBlocking {
            //Given
            coEvery { repository.getLabels() } returns anyListOfLabelModel

            //When
            val labels = getLabelsUseCase.invoke()

            //Then
            assert(anyListOfLabelModel == labels)
        }
}