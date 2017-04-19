package ru.mipt.customviewsample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import ru.mipt.customviewsample.viewgroup.FlowLayout;

public class FlowLayoutActivity extends AppCompatActivity {
    private FlowLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        fl = (FlowLayout)findViewById(R.id.flow_layout);

        final String[] fruits = {"Apple","Mango","Peach","Banana","Orange","Grapes","Watermelon","Tomato"};
        final Random rnd = new Random();

        findViewById(R.id.add_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = new TextView(FlowLayoutActivity.this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                tv.setTextColor(ContextCompat.getColor(FlowLayoutActivity.this, R.color.colorAccent));

                int idx = rnd.nextInt(fruits.length);
                tv.setText(fruits[idx]);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(4, 4, 4, 4);
                tv.setLayoutParams(layoutParams);
                fl.addView(tv);
            }
        });

        findViewById(R.id.remove_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = fl.getChildCount();
                if (count > 0) {
                    fl.removeViewAt(count - 1);
                }
            }
        });
    }
}
