package ru.mipt.customviewsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ru.mipt.customviewsample.view.TallyCounter;

/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */

public class TallyCounterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_LAYOUT_RES_ID = "layoutResId";
    public static final String EXTRA_TOAST_ON_CLICK = "toastOnClick";

    TallyCounter tallyCounter;

    private boolean toastOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final int layoutResId = intent
                .getIntExtra(EXTRA_LAYOUT_RES_ID, R.layout.activity_drawn);
        setContentView(layoutResId);
        tallyCounter = (TallyCounter)findViewById(R.id.tally_counter);
        View increment = findViewById(R.id.increment);
        if (increment != null) {
            increment.setOnClickListener(this);
        }
        View reset = findViewById(R.id.reset);
        if (reset != null) {
            reset.setOnClickListener(this);
        }

        toastOnClick = intent.getBooleanExtra(EXTRA_TOAST_ON_CLICK, false);
    }

    public static void start(Context context, @LayoutRes int layoutResId,
                               boolean toastOnClick) {
        final Intent intent = new Intent(context, TallyCounterActivity.class);
        intent.putExtra(EXTRA_LAYOUT_RES_ID, layoutResId);
        intent.putExtra(EXTRA_TOAST_ON_CLICK, toastOnClick);
        context.startActivity(intent);
    }

    void onIncrementClick() {
        tallyCounter.increment();

        if (toastOnClick) {
            Toast.makeText(this, getString(R.string.click), Toast.LENGTH_SHORT).show();
        }
    }

    void onRestClick() {
        tallyCounter.reset();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.increment:
                onIncrementClick();
                break;
            case R.id.reset:
                onRestClick();
                break;
        }
    }
}