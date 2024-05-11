package com.example.timewise.domain.model

import com.example.timewise.R

sealed class ImageModel(val image: Int) {
    data object EmptyFace:ImageModel(R.drawable.ic_empty_image)
    data object Add:ImageModel(R.drawable.ic_add_label)
    data object Calendar:ImageModel(R.drawable.ic_calendar_week)
}