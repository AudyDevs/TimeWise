package com.example.timewise.core.extensions.map

import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelEntity
import com.example.timewise.motherobject.TimeWiseMotherObject.anyLabelModel
import io.kotlintest.shouldBe
import org.junit.Test

class LabelMapKtTest {

    @Test
    fun `toRoom should return a correct LabelEntity`() {
        //Given
        val labelModel = anyLabelModel

        //When
        val labelEntity = labelModel.toRoom()
        //Then

        labelEntity.id shouldBe labelModel.id
        labelEntity.image shouldBe labelModel.image
        labelEntity.name shouldBe labelModel.name
        labelEntity.textColor shouldBe labelModel.textColor
        labelEntity.backcolor shouldBe labelModel.backcolor
    }

    @Test
    fun `toDomain should return a correct LabelModel`() {
        //Given
        val labelEntity = anyLabelEntity

        //When
        val labelModel = labelEntity.toDomain()
        //Then

        labelModel.id shouldBe labelEntity.id
        labelModel.image shouldBe labelEntity.image
        labelModel.name shouldBe labelEntity.name
        labelModel.textColor shouldBe labelEntity.textColor
        labelModel.backcolor shouldBe labelEntity.backcolor
    }
}