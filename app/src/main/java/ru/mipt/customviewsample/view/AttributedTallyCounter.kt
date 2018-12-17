package ru.mipt.customviewsample.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

import java.util.Locale

import ru.mipt.customviewsample.R

class AttributedTallyCounter(context: Context, attrs: AttributeSet? = null) : View(context, attrs), TallyCounter {
    // Some constants
    companion object {
        private const val MAX_COUNT = 9999
        private const val MAX_COUNT_STRING = MAX_COUNT.toString()
    }

    // State variables
    private var count: Int = 0
    private var displayedCount: String? = null


    // Set up paints for canvas drawing.
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val numberPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    // Allocate objects needed for canvas drawing here.
    private val backgroundRect = RectF()

    private val cornerRadius: Float

    init {
        // Get an array containing TallyCount attributes from XML.
        val typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.TallyCounter, 0, 0)

        // Get the background color from the attributes.
        val backgroundColor = typedArray.getColor(R.styleable.TallyCounter_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimary))
        backgroundPaint.color = backgroundColor

        // Get the baseline color and width from the attributes.
        val baselineColor = typedArray.getColor(R.styleable.TallyCounter_baselineColor,
                ContextCompat.getColor(context, R.color.colorAccent))
        val baselineWidth = typedArray.getDimensionPixelSize(R.styleable.TallyCounter_baselineWidth, 1)
        linePaint.color = baselineColor
        linePaint.strokeWidth = baselineWidth.toFloat()

        // Get the text color and text size from the attributes.
        val textColor = typedArray.getColor(R.styleable.TallyCounter_android_textColor,
                ContextCompat.getColor(context, android.R.color.white))
        val textSize = Math.round(
                typedArray.getDimensionPixelSize(R.styleable.TallyCounter_android_textSize,
                        Math.round(64f * resources.displayMetrics.scaledDensity)).toFloat()).toFloat()

        numberPaint.color = textColor
        numberPaint.textSize = textSize

        numberPaint.typeface = Typeface.SANS_SERIF

        // Get the corner radius.
        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.TallyCounter_cornerRadius,
                Math.round(2f * resources.displayMetrics.density)).toFloat()

        // Recycle the TypeArray. Always do this!
        typedArray.recycle()

        // Do initial count setup.
        setCount(0)
    }

    //
    // Counter interface
    //

    override fun reset() {
        setCount(0)
    }

    override fun increment() {
        setCount(count + 1)
    }

    override fun setCount(count: Int) {
        this.count = Math.min(count, MAX_COUNT)
        this.displayedCount = String.format(Locale.getDefault(), "%04d", count)
        invalidate()
    }

    override fun getCount(): Int {
        return count
    }

    /**
     * Reconcile a desired size for the view contents with a [android.view.View.MeasureSpec]
     * constraint passed by the parent.
     *
     * Simplified version of [View.resolveSizeAndState].
     *
     * @param contentSize Size of the view's contents.
     * @param measureSpec A [android.view.View.MeasureSpec] passed by the parent.
     * @return A size that best fits `contentSize` while respecting the parent's constraints.
     */
    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        return when (mode) {
            View.MeasureSpec.EXACTLY -> specSize
            View.MeasureSpec.AT_MOST -> if (contentSize < specSize) contentSize else specSize
            View.MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    //
    // View overrides
    //

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fontMetrics = numberPaint.fontMetrics

        val maxTextWidth = numberPaint.measureText(MAX_COUNT_STRING)
        val maxTextHeight = -fontMetrics.top + fontMetrics.bottom

        val desiredWidth = Math.round(maxTextWidth + paddingLeft.toFloat() + paddingRight.toFloat())
        val desiredHeight = Math.round(maxTextHeight * 2f + paddingTop.toFloat() +
                paddingBottom.toFloat())

        val measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {

        // Grab canvas dimensions.
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height

        // Calculate horizontal center.
        val centerX = canvasWidth * 0.5f

        // Draw the background.
        backgroundRect.set(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat())
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint)

        // Draw baseline.
        val baselineY = Math.round(canvasHeight * 0.6f).toFloat()
        canvas.drawLine(0f, baselineY, canvasWidth.toFloat(), baselineY, linePaint)

        // Draw text.
        val textWidth = numberPaint.measureText(displayedCount)
        val textX = Math.round(centerX - textWidth * 0.5f).toFloat()
        canvas.drawText(displayedCount!!, textX, baselineY, numberPaint)
    }
}