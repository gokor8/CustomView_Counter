package com.example.appcustomcounter.custom_view.counter_addons.mappers

import com.example.appcustomcounter.custom_view.counter_addons.measures.SizeMeasure
import com.example.appcustomcounter.custom_view.counter_addons.measures.WeightMeasure

class IntToTypeMeasureMapper(private val measureSpec: Int, private val weight: Int = 3) {

    fun map(size: Int, margin: Int) = when(size) {
        0 -> WeightMeasure(measureSpec, weight, margin)
        else -> SizeMeasure(measureSpec, size, margin)
    }
}
