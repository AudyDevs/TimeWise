package com.example.timewise.domain.model

import com.example.timewise.R

sealed class ColorModel(val textColor: Int, val backcolor: Int) {
    data object Yellow : ColorModel(R.color.textColorYellow, R.color.backColorYellow)
    data object Orange : ColorModel(R.color.textColorOrange, R.color.backColorOrange)
    data object Red : ColorModel(R.color.textColorRed, R.color.backColorRed)
    data object Purple : ColorModel(R.color.textColorPurple, R.color.backColorPurple)
    data object Blue : ColorModel(R.color.textColorBlue, R.color.backColorBlue)
    data object LightBlue : ColorModel(R.color.textColorLightBlue, R.color.backColorLightBlue)
    data object Cyan : ColorModel(R.color.textColorCyan, R.color.backColorCyan)
    data object Green : ColorModel(R.color.textColorGreen, R.color.backColorGreen)
}