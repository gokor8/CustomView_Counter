package com.example.appcustomcounter.custom_view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.example.appcustomcounter.R
import com.example.appcustomcounter.custom_view.counter_addons.ImageStateSetter
import com.example.appcustomcounter.custom_view.counter_addons.MarginType
import com.example.appcustomcounter.custom_view.counter_addons.measures.SizeMeasure
import com.example.appcustomcounter.custom_view.counter_addons.measures.WeightMeasure
import com.example.appcustomcounter.custom_view.counter_addons.models.ChangeColorsModel
import com.example.appcustomcounter.custom_view.counter_addons.models.LayoutInputSidesModel
import com.example.appcustomcounter.custom_view.counter_addons.models.LimitationsModel
import com.example.appcustomcounter.custom_view.counter_addons.calculators.LinearCalculator
import com.example.appcustomcounter.custom_view.counter_addons.chain.MaxChain
import com.example.appcustomcounter.custom_view.counter_addons.chain.ZeroConditionChain
import com.example.appcustomcounter.custom_view.counter_addons.mappers.EnumIntToMarginTypeMapper
import com.example.appcustomcounter.custom_view.counter_addons.mappers.IntToTypeMeasureMapper
import com.example.appcustomcounter.core.Handler
import com.example.appcustomcounter.core.chain.Chain
import com.example.appcustomcounter.core.chain.ChainHandlerImpl
import com.example.appcustomcounter.core.chain.ConditionChain
import java.util.*
import java.util.Objects.isNull


/**
 * Перед использованием стоит подумать и прочитать все что ниже
 * Енамы задают тип отступа внутренних объекотов от друга(button, textView, Button)
 * @property marginInDoor, желательно подумать перед использованием, ибо просто указать размерность не получится.
 * Все объекты поделены по размерам между собой, если вы не меняли им вес или не используете другой тип marginType.
 * Т.е каждый объект одинаковой ширины с другим.
 * Следовательно если указывать marginInDoor 2dp, где ширина 86dp. Получится b28-tv30-b28, отступ передается объектам в ширину
 * Формула вычисления размера для SizeMeasure. Кнопка: Ширина кнопки + marginInDoor | Текстовка: нужный размер без отступов(margin)
 **/

/**
 * @property buttonsSize = Какие-то размеры
 * @property textCounterSize = Какие-то размеры
 * @see SizeMeasure
 * Если вы задали размерность, то вычисляться размер будет иначе.
 * Если в стандартном режиме размерность подстраивается под всю ширину, то тут нет.(SizeMeasure, сколько дали размера, столько и будет)
 * Формула правильного рассчета размера(MARGIN_FROM_TEXTVIEW): Buttons(buttonsSize - marginInDoor), TextView(textView + marginInDoor)
 * Пример:
 * @sample buttonsSize = 67dp
 * @sample marginInDoor = 35dp
 * @sample textCounterSize = 8dp
 * @sample width = 142dp(Можно и match_parent, но рассчитать будет немного сложнее, посмотреть в логах размер)
 * Учитывать надо паддинги еще(отнять от общего размера, или посмотреть в логах готовый размер)
 * Итог: 67dp - 35dp(Каждая кнопка), 8dp + 35dp * 2(* 2 так как 2 кнопки) = 32,5dp(B) 79dp(Tv) 32,5dp(B)
 */

/**
 * @property buttonsSize = Пустые
 * @property textCounterSize = Пустые
 * @see WeightMeasure
 * Если вы задали размерность, то вычисляться размер будет с весом
 * Это стандартный режим(WeightMeasure), размеры детей подстроятся под ширину. Поделится на количество элементов и элементы займут всю ширину.
 * Формула правильного рассчета размера(MARGIN_FROM_TEXTVIEW): Buttons(buttonsSize - marginInDoor), TextView(textView + marginInDoor)
 * Пример:
 * @sample buttonsSize = null
 * @sample textCounterSize = null
 * @sample marginInDoor = 16dp
 * @sample width = 142dp(Например, можно и match_parent, но рассчитать будет немного сложнее, посмотреть в логах размер)
 * Учитывать надо паддинги еще(отнять от общего размера, или посмотреть в логах готовый размер)
 * Итог: 142/3 = 47dp
 * 47dp - 16dp(Каждая кнопка), 47dp + 16dp * 2(* 2 так как 2 кнопки) = 32dp(B) 78dp(Tv) 32dp(B)
 */

/**
 * @property MARGIN_TO_TEXTVIEW
 * @see WeightMeasure
 * marginInDoor отнимается от ширины кнопок и идет в ширину текстовки
 * Следовательно если указывать marginInDoor 2dp, где ширина 86dp(WeightMeasure).
 * Получится b28-tv30-b28 (86/3) , отступ передается объектам в ширину
 */

/**
 * @property MARGIN_TO_BUTTONS
 * @see WeightMeasure
 * marginInDoor отнимается от ширины текстовки и идет в ширину кнопок
 * Следовательно если указывать marginInDoor 2dp, где ширина 86dp(WeightMeasure).
 * Получится b30-tv24-b30 (86/3) , отступ передается объектам в ширину
 */

/**
 * @property marginType - тип отступа(от какого объекта будет браться ширина и к какому отдаваться)
 * @property plusButtonBackground, minusButtonBackground - drawable для кнопок(если нужен ripple, ripple вместо selector)
 * @property plusButtonSrc, minusButtonSrc - картинка для кнопки
 * @property maxCountLimit, minCountLimit - лимиты количества
 * @property srcButtonsColor - цвет для картинок на кнопки
 * @property backgroundButtonsColor - цвет для всей кнопки(крмое картинок)
 * @property disabledSrcButtonsColor - выключенный цвет для картинок на кнопки
 * @property disabledBackgroundButtonsColor - выключенный цвет фона для кнопок
 **/


class CounterWideView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    interface CounterCallback {
        fun onPlusCheckAttribute(): Int = DEFAULT_MAX_COUNT_VALUE

        fun onCountChanged(count: Int)
    }

    // Customization
    private val minusButtonSrc: Drawable?
    private val minusButtonBackground: Drawable?

    private val plusButtonSrc: Drawable?
    private val plusButtonBackground: Drawable?

    private val backgroundRadius: Float
    private val marginType: MarginType
    private val marginInDoorPercents: Int

    private val paint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
    }

    private val backgroundRippleDrawable: Drawable?

    private val buttonsSize: Int
    private val textCounterSize: Int

    // Visualization
    private lateinit var minusButtonHandler: LinearCalculator<ImageButton>
    private lateinit var counterTextHandler: LinearCalculator<TextView>
    private lateinit var plusButtonHandler: LinearCalculator<ImageButton>

    var callback: CounterCallback? = null
    var count: Int = DEFAULT_COUNT_VALUE
        set(value) {
            limitations.currentCount = value
            if(validate()) {
                field = value
                counterTextHandler.view.text = value.toString()
            }
            callback?.onCountChanged(count)
        }

    private val limitations: LimitationsModel
    private val backgroundButtonsColorsModel: ChangeColorsModel
    private val srcColorsModel: ChangeColorsModel
    private val backgroundColorsModel: ChangeColorsModel

    private lateinit var zeroConditionChain: ConditionChain<Boolean>
    private lateinit var maxChain: Chain<Boolean>
    private lateinit var chainHandler: Handler<Boolean>

    init {
        setWillNotDraw(false)

        with(context.theme.obtainStyledAttributes(attrs, R.styleable.CounterWideView, 0, 0)) {
            val maxCount =
                getInt(R.styleable.CounterWideView_maxCountLimit, DEFAULT_MAX_COUNT_VALUE)
            val minCount =
                getInt(R.styleable.CounterWideView_minCountLimit, DEFAULT_MIN_COUNT_VALUE)
            limitations = LimitationsModel(maxCount, minCount, count)

            paint.color = getColor(R.styleable.CounterWideView_backgroundColor, Color.TRANSPARENT)
            backgroundRadius =
                getDimensionPixelSize(R.styleable.CounterWideView_backgroundRadius, 0).toFloat()

            minusButtonSrc = getDrawable(R.styleable.CounterWideView_minusButtonSrc)
            minusButtonBackground = getDrawable(R.styleable.CounterWideView_minusButtonBackground)

            plusButtonSrc = getDrawable(R.styleable.CounterWideView_plusButtonSrc)
            plusButtonBackground = getDrawable(R.styleable.CounterWideView_plusButtonBackground)

            marginInDoorPercents = getInt(
                R.styleable.CounterWideView_marginInDoorPercents, 0
            )

            val marginEnumInt = getInt(R.styleable.CounterWideView_marginType, 0)
            marginType = EnumIntToMarginTypeMapper(marginInDoorPercents).map(marginEnumInt)

            buttonsSize = getDimensionPixelSize(R.styleable.CounterWideView_buttonsSize, 0)
            textCounterSize = getDimensionPixelSize(R.styleable.CounterWideView_textCounterSize, 0)

            val backgroundColor =
                getColor(R.styleable.CounterWideView_backgroundButtonsColor, Color.TRANSPARENT)

            backgroundRippleDrawable = getDrawable(R.styleable.CounterWideView_backgroundRipple)

            val disabledBackgroundColor =
                getColor(
                    R.styleable.CounterWideView_disabledBackgroundButtonsColor,
                    Color.TRANSPARENT
                )
            val backgroundButtonsColor =
                getColor(R.styleable.CounterWideView_backgroundButtonsColor, Color.TRANSPARENT)
            val disabledBackgroundButton =
                getColor(
                    R.styleable.CounterWideView_disabledBackgroundButtonsColor,
                    Color.TRANSPARENT
                )
            val buttonsSrcColor =
                getColor(R.styleable.CounterWideView_srcButtonsColor, Color.BLUE)
            val disabledSrcButtonsColor =
                getColor(R.styleable.CounterWideView_disabledSrcButtonsColor, buttonsSrcColor)


            backgroundButtonsColorsModel = ChangeColorsModel(
                backgroundButtonsColor,
                disabledBackgroundButton
            )
            srcColorsModel = ChangeColorsModel(
                buttonsSrcColor,
                disabledSrcButtonsColor
            )
            backgroundColorsModel = ChangeColorsModel(
                backgroundColor,
                disabledBackgroundColor
            )

            setInitAddons()

           // count = DEFAULT_COUNT_VALUE

            recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var layoutLeft = paddingLeft

        layoutLeft = minusButtonHandler.layout(
            LayoutInputSidesModel(layoutLeft)
        )

        layoutLeft = counterTextHandler.layout(
            LayoutInputSidesModel(layoutLeft)
        )

        plusButtonHandler.layout(
            LayoutInputSidesModel(layoutLeft)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val treatedWidthSpec = widthMeasureSpec - paddingStart - paddingEnd
        val treatedHeightSpec = heightMeasureSpec - paddingTop - paddingBottom

        val widthSize = MeasureSpec.getSize(treatedWidthSpec)

        val mapperIntSizeToMeasure =
            IntToTypeMeasureMapper(treatedWidthSpec, 3)

        minusButtonHandler.measure(
            mapperIntSizeToMeasure.map(buttonsSize, marginType.getButtonMargin(widthSize)),
            WeightMeasure(treatedHeightSpec, DEFAULT_WEIGHT)
        )
        counterTextHandler.measure(
            mapperIntSizeToMeasure.map(textCounterSize, marginType.getTextViewMargin(widthSize)),
            WeightMeasure(treatedHeightSpec, DEFAULT_WEIGHT)
        )
        plusButtonHandler.measure(
            mapperIntSizeToMeasure.map(buttonsSize, marginType.getButtonMargin(widthSize)),
            WeightMeasure(treatedHeightSpec, DEFAULT_WEIGHT)
        )

        setMeasuredDimension(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val floatBackgroundRadius = backgroundRadius
        val floatPaddingTop = paddingTop.toFloat()
        val contentWidth = width
        val contentHeight = height

        canvas?.drawRoundRect(
            RectF(
                0f,
                floatPaddingTop,
                contentWidth.toFloat(),
                floatPaddingTop + contentHeight
            ), floatBackgroundRadius, floatBackgroundRadius, paint
        ).apply {
            foreground = backgroundRippleDrawable
            setOnClickListener(null)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(COUNTER_VIEW_STATE_TAG, super.onSaveInstanceState())
            putInt(COUNTER_VIEW_COUNT_TAG, count)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (state is Bundle) {
            count = state.getInt(COUNTER_VIEW_COUNT_TAG)
            newState = state.getParcelable(COUNTER_VIEW_STATE_TAG)
        }
        super.onRestoreInstanceState(newState)
    }

    // TODO сделать нормально, вроде сделал нормально
    fun validate(): Boolean {
        limitations.maxCount = callback?.onPlusCheckAttribute() ?: DEFAULT_MAX_COUNT_VALUE
        return chainHandler.handle()
    }

    private fun Int.pxToDp(): Int {
        return (this / Resources.getSystem().displayMetrics.density).toInt()
    }

    private inline fun <I> setCurrentListener(
        nullableObject: I,
        viewWasListener: View,
        crossinline inListener: (v: View, event: MotionEvent?) -> Unit
    ) {
        val isNullable = isNull(nullableObject)

        if (!isNullable) {
            viewWasListener.isClickable = isNullable
            viewWasListener.setOnTouchListener { v, event ->
                inListener(v, event)
                performClick()
            }
        } else {
            viewWasListener.setOnClickListener {
                inListener(it, null)
            }
        }
    }

    private fun setInitAddons() {
        minusButtonHandler = LinearCalculator(
            AppCompatImageButton(context).apply {
                background = minusButtonBackground
                setImageDrawable(minusButtonSrc)
                ImageStateSetter(this).setDrawableColor(this.drawable, srcColorsModel.defaultColor)

                setCurrentListener(backgroundRippleDrawable, this) { _, _ ->
                    count--
                }

                addView(this)
            })

        counterTextHandler = LinearCalculator(
            TextView(context).apply {
                setTextColor(Color.BLACK)
                text = DEFAULT_COUNT_VALUE.toString()
                gravity = Gravity.CENTER
                textAlignment = TEXT_ALIGNMENT_CENTER

                addView(this)
            })

        plusButtonHandler = LinearCalculator(
            AppCompatImageButton(context).apply {
                background = plusButtonBackground
                setImageDrawable(plusButtonSrc)
                ImageStateSetter(this).setDrawableColor(this.drawable, srcColorsModel.defaultColor)

                setCurrentListener(backgroundRippleDrawable, this) { _, _ ->
                    count++
                }

                addView(this)
            })

        zeroConditionChain = ZeroConditionChain(
            limitations,
            minusButtonHandler.view,
            backgroundButtonsColorsModel,
            srcColorsModel
        )
        maxChain = MaxChain(
            limitations,
            plusButtonHandler.view,
            backgroundButtonsColorsModel,
            srcColorsModel
        )

        chainHandler = ChainHandlerImpl(zeroConditionChain, maxChain)
    }


    companion object {
        private const val DEFAULT_MIN_COUNT_VALUE = 0
        private const val DEFAULT_MAX_COUNT_VALUE = 999
        private const val DEFAULT_COUNT_VALUE = 0
        private const val DEFAULT_WEIGHT = 1
        private const val COUNTER_VIEW_STATE_TAG = "counter_wide_view_state"
        private const val COUNTER_VIEW_COUNT_TAG = "counter_wide_view_count"
    }
}