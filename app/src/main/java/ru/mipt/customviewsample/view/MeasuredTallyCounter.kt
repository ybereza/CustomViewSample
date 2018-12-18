package ru.mipt.customviewsample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

import java.util.Locale

import ru.mipt.customviewsample.R

/**
 * Constructor that is called when inflating a view from XML. This is called
 * when a view is being constructed from an XML file, supplying attributes
 * that were specified in the XML file.
 *
 * @param context The Context the view is running in, through which it can
 * access the current theme, resources, etc.
 * @param attrs   The attributes of the XML tag that is inflating the view.
 */
class MeasuredTallyCounter(context: Context, attrs: AttributeSet? = null) : View(context, attrs), TallyCounter {

    private val MAX_COUNT = 9999
    private val MAX_COUNT_STRING = MAX_COUNT.toString()

    // State variables
    private var count = 0
    private var displayedCount = ""

    // Drawing variables
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val numberPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    // Allocate objects needed for canvas drawing here.
    private val backgroundRect = RectF()

    private val cornerRadius: Float

    init {

        // Set up paints for canvas drawing.
        backgroundPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)

        linePaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        linePaint.strokeWidth = 1f

        numberPaint.color = ContextCompat.getColor(context, android.R.color.white)
        numberPaint.textSize = Math.round(64f * resources.displayMetrics.scaledDensity).toFloat()

        // Initialize drawing measurements.
        cornerRadius = Math.round(2f * resources.displayMetrics.density).toFloat()

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
     * This is a simplified version of [View.resolveSize]
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fontMetrics = numberPaint.fontMetrics

        // Measure maximum possible width of text.
        val maxTextWidth = numberPaint.measureText(MAX_COUNT_STRING)
        // Estimate maximum possible height of text.
        val maxTextHeight = fontMetrics.bottom - fontMetrics.top

        // Add padding to maximum width calculation.
        val desiredWidth = Math.round(maxTextWidth + paddingLeft.toFloat() + paddingRight.toFloat())

        // Add padding to maximum height calculation.
        val desiredHeight = Math.round(maxTextHeight * 2f + paddingTop.toFloat() +
                paddingBottom.toFloat())

        // Reconcile size that this view wants to be with the size the parent will let it be.
        val measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec)

        // Store the final measured dimensions.
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

        // Draw lines that show font top and bottom.
        val fontMetrics = numberPaint.fontMetrics
        val topY = Math.round(baselineY + fontMetrics.top).toFloat()
        val bottomY = Math.round(baselineY + fontMetrics.bottom).toFloat()
        canvas.drawLine(0f, topY, canvasWidth.toFloat(), topY, linePaint)
        canvas.drawLine(0f, bottomY, canvasWidth.toFloat(), bottomY, linePaint)

        // Draw text.
        val textWidth = numberPaint.measureText(displayedCount)
        val textX = Math.round(centerX - textWidth * 0.5f).toFloat()
        canvas.drawText(displayedCount, textX, baselineY, numberPaint)
    }

}