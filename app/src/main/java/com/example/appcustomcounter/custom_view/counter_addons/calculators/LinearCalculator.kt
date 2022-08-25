package com.example.appcustomcounter.custom_view.counter_addons.calculators

import android.view.View
import com.example.appcustomcounter.custom_view.counter_addons.models.LayoutInputSidesModel

class LinearCalculator<V: View>(override val view: V) :
    CustomViewCalculator<Int, LayoutInputSidesModel> {

    override fun layout(sizes: LayoutInputSidesModel): Int {
        val rightPixel = sizes.leftPixel + view.measuredWidth

        view.layout(sizes.leftPixel,0,rightPixel, view.measuredHeight)

        return rightPixel
    }
}
