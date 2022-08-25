package com.example.appcustomcounter.custom_view.counter_addons.calculators

import android.view.View
import com.example.appcustomcounter.custom_view.counter_addons.measures.Measure
import com.example.appcustomcounter.custom_view.counter_addons.models.LayoutInputSidesModel

interface CustomViewCalculator<T : Any, I: LayoutInputSidesModel> {

    val view: View

    fun layout(sizes: I): T

    fun measure(separateWidth: Measure, separateHeight: Measure) =
        view.measure(separateWidth.measure(), separateHeight.measure())
}
