package com.example.appcustomcounter.custom_view.counter_addons.measures

import android.view.View

class SizeMeasure(
    private val measureSpec: Int,
    private val size: Int,
    override val margin: Int = 0
) : MarginMeasure {

    override fun measure() = when (View.MeasureSpec.getMode(measureSpec)) {
        View.MeasureSpec.UNSPECIFIED -> measureSpec
        View.MeasureSpec.AT_MOST -> weightMeasure(size, View.MeasureSpec.AT_MOST)
        View.MeasureSpec.EXACTLY -> weightMeasure(size, View.MeasureSpec.EXACTLY)
        else -> error("Unreachable")
    }

    private fun weightMeasure(size: Int, measureMode: Int) = View.MeasureSpec.makeMeasureSpec(
        size + margin,
        measureMode
    )
}
