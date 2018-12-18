package ru.mipt.customviewsample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import ru.mipt.customviewsample.view.TallyCounter

/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */

class TallyCounterActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var tallyCounter: TallyCounter

    private var toastOnClick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val layoutResId = intent
                .getIntExtra(EXTRA_LAYOUT_RES_ID, R.layout.activity_drawn)
        setContentView(layoutResId)
        tallyCounter = findViewById<View>(R.id.tally_counter) as TallyCounter
        val increment = findViewById<View>(R.id.increment)
        increment?.setOnClickListener(this)
        val reset = findViewById<View>(R.id.reset)
        reset?.setOnClickListener(this)

        toastOnClick = intent.getBooleanExtra(EXTRA_TOAST_ON_CLICK, false)
    }

    private fun onIncrementClick() {
        tallyCounter.increment()

        if (toastOnClick) {
            Toast.makeText(this, getString(R.string.click), Toast.LENGTH_SHORT).show()
        }
    }

    internal fun onRestClick() {
        tallyCounter.reset()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.increment -> onIncrementClick()
            R.id.reset -> onRestClick()
        }
    }

    companion object {
        val EXTRA_LAYOUT_RES_ID = "layoutResId"
        val EXTRA_TOAST_ON_CLICK = "toastOnClick"

        fun start(context: Context, @LayoutRes layoutResId: Int,
                  toastOnClick: Boolean) {
            val intent = Intent(context, TallyCounterActivity::class.java)
            intent.putExtra(EXTRA_LAYOUT_RES_ID, layoutResId)
            intent.putExtra(EXTRA_TOAST_ON_CLICK, toastOnClick)
            context.startActivity(intent)
        }
    }
}