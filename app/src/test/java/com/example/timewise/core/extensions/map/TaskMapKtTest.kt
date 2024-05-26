package com.example.timewise.core.extensions.map

import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskEntity
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
import io.kotlintest.shouldBe
import org.junit.Test

class TaskMapKtTest {

    @Test
    fun `toRoom should return a correct TaskEntity`() {
        //Given
        val taskModel = anyTaskModel

        //When
        val taskEntity = taskModel.toRoom()

        //Then
        taskEntity.id shouldBe taskModel.id
        taskEntity.idLabel shouldBe taskModel.idLabel
        taskEntity.name shouldBe taskModel.name
        taskEntity.isFinished shouldBe taskModel.isFinished
        taskEntity.finishedDate shouldBe taskModel.finishedDate
        taskEntity.isFavourite shouldBe taskModel.isFavourite
        taskEntity.reminderDate shouldBe taskModel.reminderDate
        taskEntity.expirationDate shouldBe taskModel.expirationDate
        taskEntity.details shouldBe taskModel.details
        taskEntity.creationDate shouldBe taskModel.creationDate
    }

    @Test
    fun `toDomain should return a correct TaskModel`() {
        //Given
        val taskEntity = anyTaskEntity

        //When
        val taskModel = taskEntity.toDomain()

        //Then
        taskModel.id shouldBe taskEntity.id
        taskModel.idLabel shouldBe taskEntity.idLabel
        taskModel.name shouldBe taskEntity.name
        taskModel.isFinished shouldBe taskEntity.isFinished
        taskModel.finishedDate shouldBe taskEntity.finishedDate
        taskModel.isFavourite shouldBe taskEntity.isFavourite
        taskModel.reminderDate shouldBe taskEntity.reminderDate
        taskModel.expirationDate shouldBe taskEntity.expirationDate
        taskModel.details shouldBe taskEntity.details
        taskModel.creationDate shouldBe taskEntity.creationDate
    }
}