package com.example.appcustomcounter.custom_view.counter_addons.chain

import android.widget.ImageView
import com.example.appcustomcounter.custom_view.counter_addons.ImageStateSetter
import com.example.appcustomcounter.custom_view.counter_addons.models.ChangeColorsModel
import com.example.appcustomcounter.custom_view.counter_addons.models.LimitationsModel
import com.example.appcustomcounter.core.chain.ConditionChain

class ZeroConditionChain<V : ImageView>(
    private val limitations: LimitationsModel,
    override val view: V,
    override val backgroundColorsModel: ChangeColorsModel,
    override val srcColorsModel: ChangeColorsModel,
) : ImageViewChain<V>, ConditionChain.BaseConditionChain<Boolean>() {

    override val imageViewChain: ImageStateSetter = ImageStateSetter(view)


    override fun condition(): Boolean
        = limitations.currentCount == limitations.minCount - 1
}
