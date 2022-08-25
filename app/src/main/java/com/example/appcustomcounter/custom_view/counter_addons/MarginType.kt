package com.example.appcustomcounter.custom_view.counter_addons

sealed class MarginType(protected val marginPercents: Int) {

    abstract fun getButtonMargin(measureSpec: Int): Int
    abstract fun getTextViewMargin(measureSpec: Int): Int


    protected fun mapPercents(measureSpec: Int, percents: Int) = (measureSpec / 100f * percents).toInt()


    class MarginFromButtons(measureSpec: Int) : MarginType(measureSpec) {
        override fun getButtonMargin(measureSpec: Int) = mapPercents(measureSpec, marginPercents)
        override fun getTextViewMargin(measureSpec: Int) =
            mapPercents(measureSpec, marginPercents) + mapPercents(
                measureSpec,
                marginPercents
            ) - (mapPercents(measureSpec, marginPercents) + mapPercents(
                measureSpec,
                marginPercents
            )) * 2
    }

    class MarginFromTextView(marginPercents: Int) : MarginType(marginPercents) {
        override fun getButtonMargin(measureSpec: Int) =
            mapPercents(measureSpec, marginPercents) - (mapPercents(
                measureSpec,
                marginPercents
            ) * 2)

        override fun getTextViewMargin(measureSpec: Int) =
            mapPercents(measureSpec, marginPercents) + mapPercents(measureSpec, marginPercents)
    }
}
