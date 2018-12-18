package ru.mipt.customviewsample.viewgroup


import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

class FlowLayout : ViewGroup {

    private var gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
    private val widthList = ArrayList<Int>()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 10

        /*
         * Рассмотрим ширину детей
         *      если она MATCH_PARENT то ширина будет наша ширина минус наш padding по горизонтали
         *      если на WRAP_CONTENT то ширина будет шириной ребенка
         *      тоже самое с высотой
         *
         *
         * Ширина: Если специцикация EXACTLY - нам надо вернуть ширину которую от нас хотят
         *                           AT_MOST - надо посчитать сколько детей влезает по ширине но не болше заданого
         *                           UNSPECIFIED - надо считать детей в линеечку по ширине
		 *	                              Надо смотреть родителя тогда и от него плясать
         *
         * Высота: Если спецификация EXACTLY - надо вернуть то что от нас хотят
         *                           AT_MOST - надо вернуть не больше чем от нас хотят
         *                           UNSPECIFIED - надо посчитать высоту в зависимости от того расположены дети
         */


        var lineWidth = 0
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v.visibility == View.GONE) { // Не виден и не занимает места
                continue
            }
            val lp = v.layoutParams as LayoutParams
            var childWidthType = View.MeasureSpec.EXACTLY
            var childWidth = lp.width
            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                childWidthType = View.MeasureSpec.EXACTLY
                childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
            } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                childWidthType = View.MeasureSpec.AT_MOST
                childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
            } // else default value

            var childHeightType = View.MeasureSpec.UNSPECIFIED
            var childHeight = 0

            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                childHeightType = View.MeasureSpec.AT_MOST
                childHeight = heightSize - paddingTop - paddingBottom + lp.topMargin + lp.bottomMargin
            } else if (lp.height >= 0) {
                childHeightType = View.MeasureSpec.EXACTLY
                childHeight = lp.height
            }

            v.measure(
                    View.MeasureSpec.makeMeasureSpec(childWidth, childWidthType),
                    View.MeasureSpec.makeMeasureSpec(childHeight, childHeightType)
            )

            val childWidthReal = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeightReal = v.measuredHeight + lp.topMargin + lp.bottomMargin
            if (lineWidth + childWidthReal > widthSize) { // Переходим на следую
                width = Math.max(lineWidth, childWidthReal)
                lineWidth = childWidthReal
                height += childHeightReal
            } else {
                lineWidth += childWidthReal
                height = Math.max(height, childHeightReal)
            }
        }
        width = Math.max(lineWidth, width)

        width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize else width
        height = if (heightMode == View.MeasureSpec.EXACTLY) heightSize else height
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, in_l: Int, in_t: Int, r: Int, b: Int) {
        /*
        * 1) Надо пробежаться по всем детям посчитать максимальную ширину линии и максиманую высоту
        * 2) Исходя из настроек гравити расставить элементы
        */
        var height = 0
        var lineWidth = 0
        val childCount = childCount
        widthList.clear()
        val widthSize = measuredWidth
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v.visibility == View.GONE)
                continue
            val lp = v.layoutParams as LayoutParams
            val childWidth = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = v.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidth > widthSize) {  // Новая линия
                widthList.add(lineWidth)
                lineWidth = childWidth
                height += childHeight
            } else {
                lineWidth += childWidth
                height = Math.max(height, childHeight)
            }
        }
        widthList.add(lineWidth)

        var verticalGravityMargin = 0
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.BOTTOM -> verticalGravityMargin = getHeight() - height
            Gravity.CENTER_VERTICAL -> verticalGravityMargin = (getHeight() - height) / 2
            Gravity.TOP -> {
            }
            else -> {
            }
        }

        val globalWidth = measuredWidth
        lineWidth = 0
        var top = verticalGravityMargin
        var left = 0
        var currentLineWidth: Int
        var lineCnt = 0
        var t: Int
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v.visibility == View.GONE)
                continue
            val lp = v.layoutParams as LayoutParams
            val childWidth = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = v.measuredHeight + lp.topMargin + lp.bottomMargin

            var l = left

            if (i == 0 || lineWidth + childWidth > widthSize) {  // Новая линия
                lineWidth = childWidth
                currentLineWidth = widthList[lineCnt]
                ++lineCnt
                var lineHorizontalGravityMargin = 0
                when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                    Gravity.CENTER_HORIZONTAL -> lineHorizontalGravityMargin = (globalWidth - currentLineWidth) / 2
                    Gravity.RIGHT -> lineHorizontalGravityMargin = globalWidth - currentLineWidth
                    Gravity.LEFT -> {
                    }
                    else -> {
                    }
                }
                left = lineHorizontalGravityMargin
                l = left
                left += childWidth
                top += childHeight
            } else {
                left += childWidth
                lineWidth += childWidth
            }
            t = top - childHeight
            v.layout(l + lp.leftMargin, t + lp.topMargin, l + childWidth - lp.rightMargin, t + childHeight - lp.bottomMargin)
        }
    }

    fun setGravity(gravity: Int) {
        if (this.gravity != gravity) {
            this.gravity = gravity
            requestLayout()
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return FlowLayout.LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }


    class LayoutParams : ViewGroup.MarginLayoutParams {

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {}

        constructor(source: ViewGroup.LayoutParams) : super(source) {}

        constructor(width: Int, height: Int) : super(width, height) {}
    }

}