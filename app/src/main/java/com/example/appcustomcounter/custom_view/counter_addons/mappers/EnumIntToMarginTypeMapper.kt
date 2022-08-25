package com.example.appcustomcounter.custom_view.counter_addons.mappers

import com.example.appcustomcounter.custom_view.counter_addons.MarginType

class EnumIntToMarginTypeMapper(var marginPercents: Int) {

    fun map(enumValue: Int) = when (enumValue) {
        0 -> MarginType.MarginFromTextView(marginPercents)
        else -> MarginType.MarginFromButtons(marginPercents)
    }
}