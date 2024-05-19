package com.example.timewise.data

import com.example.timewise.core.extensions.map.toDomain
import com.example.timewise.core.extensions.map.toRoom
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.domain.LabelRepository
import com.example.timewise.domain.model.LabelModel
import javax.inject.Inject

class LabelRepositoryImpl @Inject constructor(
    private val labelDao: LabelDao
) : LabelRepository {

    override suspend fun getLabels(): List<LabelModel> {
        val response = labelDao.getAllLabels()
        return response.map { it.toDomain() }
    }

    override suspend fun getLabelId(id: Int): LabelModel {
        val response = labelDao.getLabelID(id)
        return response.toDomain()
    }

    override suspend fun insertLabel(label: LabelModel) {
        val response = label.toRoom()
        labelDao.insertLabel(response)
    }

    override suspend fun updateLabel(label: LabelModel) {
        val response = label.toRoom()
        labelDao.updateLabel(response)
    }

    override suspend fun deleteLabel(label: LabelModel) {
        val response = label.toRoom()
        labelDao.deleteLabel(response)
    }
}