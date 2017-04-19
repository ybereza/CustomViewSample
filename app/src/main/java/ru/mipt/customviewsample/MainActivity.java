package ru.mipt.customviewsample;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] buttons = {R.id.drawn, R.id.invalidated, R.id.measured, R.id.attributed, R.id.interacted};
        for (int buttonId : buttons) {
            findViewById(buttonId).setOnClickListener(this);
        }

        findViewById(R.id.flow_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FlowLayoutActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        boolean toastOnClick = false;
        final int layoutResId;

        switch (view.getId()) {
            case R.id.drawn:
                layoutResId = R.layout.activity_drawn;
                toastOnClick = true;
                break;
            case R.id.invalidated:
                layoutResId = R.layout.activity_invalidated;
                break;
            case R.id.measured:
                layoutResId = R.layout.activity_measured;
                break;
            case R.id.attributed:
                layoutResId = R.layout.activity_attributed;
                break;
            case R.id.interacted:
                layoutResId = R.layout.activity_interacted;
                break;
            default:
                layoutResId = -1;
                break;
        }

        if (layoutResId == -1) {
            return;
        }

        TallyCounterActivity.start(this, layoutResId, toastOnClick);
    }
}
