package com.example.appcustomcounter.custom_view.counter_addons.measures

import android.view.View
import android.view.ViewGroup

class WeightMeasure(
    private val measureSpec: Int,
    private val weight: Int,
    override val margin: Int = 0
) : MarginMeasure {

    override fun measure() = when (View.MeasureSpec.getMode(measureSpec)) {
        View.MeasureSpec.UNSPECIFIED -> measureSpec
        View.MeasureSpec.AT_MOST -> ViewGroup.LayoutParams.WRAP_CONTENT
        View.MeasureSpec.EXACTLY -> {
            View.MeasureSpec.makeMeasureSpec(
                (View.MeasureSpec.getSize(measureSpec) / weight) + margin,
                View.MeasureSpec.EXACTLY
            )
        }
        else -> error("Unreachable")
    }
}
