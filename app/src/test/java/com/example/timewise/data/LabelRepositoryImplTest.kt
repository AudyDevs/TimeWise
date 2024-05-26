package com.example.timewise.data

import com.example.timewise.core.extensions.map.toDomain
import com.example.timewise.core.extensions.map.toRoom
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelEntity
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfLabelEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LabelRepositoryImplTest {

    @MockK
    private lateinit var labelDao: LabelDao
    private lateinit var labelRepositoryImpl: LabelRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        labelRepositoryImpl = LabelRepositoryImpl(labelDao)
    }

    @Test
    fun `when LabelRepositoryImpl call getLabels successfully, it should return the labels`() =
        runBlocking {
            //Given
            val anyLabelsModel = anyListOfLabelEntity.map { it.toDomain() }
            coEvery { labelDao.getAllLabels() } returns anyListOfLabelEntity

            //When
            val labelsModelRepository = labelRepositoryImpl.getLabels()

            //Then
            assert(labelsModelRepository == anyLabelsModel)
        }

    @Test
    fun `when LabelRepositoryImpl call getLabelId successfully, it should return a correct LabelModel`() =
        runBlocking {
            //Given
            val anyLabelModel = anyLabelEntity.toDomain()
            coEvery { labelDao.getLabelID(ANY_ID_LABEL) } returns anyLabelEntity

            //When
            val labelModelRepository = labelRepositoryImpl.getLabelId(ANY_ID_LABEL)

            //Then
            assert(labelModelRepository == anyLabelModel)
        }

    @Test
    fun `when LabelRepositoryImpl call insertLabel successfully, it should call insertLabel from LabelDao once`() =
        runBlocking {
            //Given
            val labelEntity = anyLabelModel.toRoom()
            coEvery { labelDao.insertLabel(labelEntity) } just runs

            //When
            labelRepositoryImpl.insertLabel(anyLabelModel)

            //Then
            coVerify(exactly = 1) { labelDao.insertLabel(labelEntity) }
        }

    @Test
    fun `when LabelRepositoryImpl call updateLabel successfully, it should call updateLabel from LabelDao once`() =
        runBlocking {
            //Given
            val labelEntity = anyLabelModel.toRoom()
            coEvery { labelDao.updateLabel(labelEntity) } just runs

            //When
            labelRepositoryImpl.updateLabel(anyLabelModel)

            //Then
            coVerify(exactly = 1) { labelDao.updateLabel(labelEntity) }
        }

    @Test
    fun `when LabelRepositoryImpl call deleteLabel successfully, it should call deleteLabel from LabelDao once`() =
        runBlocking {
            //Given
            val labelEntity = anyLabelModel.toRoom()
            coEvery { labelDao.deleteLabel(labelEntity) } just runs

            //When
            labelRepositoryImpl.deleteLabel(anyLabelModel)

            //Then
            coVerify(exactly = 1) { labelDao.deleteLabel(labelEntity) }
        }
}