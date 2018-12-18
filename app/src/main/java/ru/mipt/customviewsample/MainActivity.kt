package ru.mipt.customviewsample

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttons = intArrayOf(R.id.drawn, R.id.invalidated, R.id.measured, R.id.attributed, R.id.interacted)
        for (buttonId in buttons) {
            findViewById<View>(buttonId).setOnClickListener(this)
        }

        findViewById<View>(R.id.flow_layout).setOnClickListener {
            val i = Intent(this@MainActivity, FlowLayoutActivity::class.java)
            startActivity(i)
        }
    }

    override fun onClick(view: View) {
        var toastOnClick = false
        val layoutResId: Int

        when (view.id) {
            R.id.drawn -> {
                layoutResId = R.layout.activity_drawn
                toastOnClick = true
            }
            R.id.invalidated -> layoutResId = R.layout.activity_invalidated
            R.id.measured -> layoutResId = R.layout.activity_measured
            R.id.attributed -> layoutResId = R.layout.activity_attributed
            R.id.interacted -> layoutResId = R.layout.activity_interacted
            else -> layoutResId = -1
        }

        if (layoutResId == -1) {
            return
        }

        TallyCounterActivity.start(this, layoutResId, toastOnClick)
    }
}
