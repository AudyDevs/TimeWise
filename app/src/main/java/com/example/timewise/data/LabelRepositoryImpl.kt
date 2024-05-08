package com.example.timewise.data

import android.util.Log
import com.example.timewise.core.extensions.map.label.toDomain
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.domain.LabelRepository
import com.example.timewise.domain.model.LabelModel
import javax.inject.Inject

class LabelRepositoryImpl @Inject constructor(private val labelDao: LabelDao) : LabelRepository {
    override suspend fun getLabels(): List<LabelModel>? {
        runCatching { labelDao.getAllLabels() }
            .onSuccess {
                val response = labelDao.getAllLabels()
                return response.map { it.toDomain() }
            }
            .onFailure { Log.i("AudyDev", "Error: ${it.message}") }
        return null
    }
}