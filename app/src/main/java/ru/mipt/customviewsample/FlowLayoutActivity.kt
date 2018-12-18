package ru.mipt.customviewsample

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.Random

import ru.mipt.customviewsample.viewgroup.FlowLayout

class FlowLayoutActivity : AppCompatActivity() {
    private lateinit var fl: FlowLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flowlayout)
        fl = findViewById(R.id.flow_layout)

        val fruits = arrayOf("Apple", "Mango", "Peach", "Banana", "Orange", "Grapes", "Watermelon", "Tomato")
        val rnd = Random()

        findViewById<View>(R.id.add_letter).setOnClickListener {
            val tv = TextView(this@FlowLayoutActivity)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            tv.setTextColor(ContextCompat.getColor(this@FlowLayoutActivity, R.color.colorAccent))

            val idx = rnd.nextInt(fruits.size)
            tv.text = fruits[idx]
            val layoutParams = FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(4, 4, 4, 4)
            tv.layoutParams = layoutParams
            fl.addView(tv)
        }

        findViewById<View>(R.id.remove_letter).setOnClickListener {
            val count = fl.childCount
            if (count > 0) {
                fl.removeViewAt(count - 1)
            }
        }
    }
}
