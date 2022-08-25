package com.example.appcustomcounter.custom_view.counter_addons.chain

import android.widget.ImageView
import com.example.appcustomcounter.custom_view.counter_addons.ImageStateSetter
import com.example.appcustomcounter.custom_view.counter_addons.models.ChangeColorsModel
import com.example.appcustomcounter.core.chain.Chain

interface ImageViewChain<V : ImageView> : Chain.MutableTriggeredChain<Boolean> {

    val view: V
    val backgroundColorsModel: ChangeColorsModel
    val srcColorsModel: ChangeColorsModel

    val imageViewChain : ImageStateSetter


    override fun isTriggered() : Boolean {
        imageViewChain.setFrontendState(false, backgroundColorsModel.disabledColor, srcColorsModel.disabledColor)
        return false
    }

    override fun isNotTriggered() : Boolean {
        imageViewChain.setFrontendState(true, backgroundColorsModel.defaultColor, srcColorsModel.defaultColor)
        return true
    }
}
