package com.example.appcustomcounter.custom_view.counter_addons

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat

class ImageStateSetter(private val view: ImageView) {

    fun setFrontendState(isEnabled: Boolean, background: Int, drawable: Int) {
        view.isEnabled = isEnabled
        setDrawableColor(view.background, background)
        setDrawableColor(view.drawable, drawable)
    }

    fun setDrawableColor(drawable: Drawable, toColor: Int) {
        DrawableCompat.setTint(
            DrawableCompat.wrap(drawable),
            toColor
        )
    }
}