package com.example.timewise.data.providers

import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.LabelModel.Compra
import com.example.timewise.domain.model.LabelModel.Juegos
import com.example.timewise.domain.model.LabelModel.Proyectos
import javax.inject.Inject

class HomeProvider @Inject constructor() {
    fun getLabels(): List<LabelModel> {
        return listOf(
            Proyectos,
            Compra,
            Juegos
        )
    }
}