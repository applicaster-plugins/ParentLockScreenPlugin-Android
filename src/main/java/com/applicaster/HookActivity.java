package com.applicaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.applicaster.pinlockview.IndicatorDots;
import com.applicaster.pinlockview.PinLockView;
import com.applicaster.web.plugins.iai.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.Random;

import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_COMPLETED;
import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_FAILED;
import static com.applicaster.hook_screen.HookScreenManager.HOOK_PROPS_EXTRA;

public class HookActivity extends AppCompatActivity  implements PinLockView.PinLockListener{


    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    public static int ANIMATION_DURATION = 400;
    public static int BUTTON_COUNT = 9;
    private int[] sequence = new int[3];
    private String[] numbers = new String[]{"z", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private TextView sequenceText;
    private View mFrame;
    private TextView sequenceText_2;
    private View mBack9;
    private View mBack3;
    private Map<String, Object> hookProps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        hookProps = convertHookMap(getIntent().getStringExtra(HOOK_PROPS_EXTRA));
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setDigitLength(BUTTON_COUNT);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        sequenceText = (TextView) findViewById(R.id.text_2);
        sequenceText_2 = (TextView) findViewById(R.id.text_3);
        sequenceText_2.setVisibility(View.GONE);
        findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActivityResult(HookActivity.this, hookProps, ACTIVITY_HOOK_FAILED);
            }
        });
        mFrame = findViewById(R.id.frame);
        mBack9 = findViewById(R.id.calulator_image_9);
        mBack3 = findViewById(R.id.calulator_image_3);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        if (BUTTON_COUNT == 3) {
            mBack3.setVisibility(View.VISIBLE);
        }else {
            mBack9.setVisibility(View.VISIBLE);
        }
        mPinLockView.setPinLockListener(this);
        generateRandom();



    }

    private void generateRandom() {
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            sequence[i] = r.nextInt(BUTTON_COUNT) + 1;
        }
        sequenceText.setText("" + numbers[sequence[0]] + ", " + numbers[sequence[1]] + ", " + numbers[sequence[2]]);
        sequenceText_2.setText("" + numbers[sequence[0]] + ", " + numbers[sequence[1]] + ", " + numbers[sequence[2]]);
    }


    @Override
    public void onPinChange(int pinLength, String pin) {
        if (pin.length() < 3) {
            setTextColor(sequenceText, R.color.perp_text_2);
        } else {
            if (pin.equals("" + sequence[0] + sequence[1] + sequence[2])) {
                sendActivityResult(this, hookProps, ACTIVITY_HOOK_COMPLETED);
            } else {
                setTextColor(sequenceText, R.color.red);
                mPinLockView.resetPinLockView();
                generateRandom();
            }
        }
    }

    private void setTextColor(TextView sequenceText, int color) {
        sequenceText.setTextColor(ContextCompat.getColor(this, color));
    }
    private void sendActivityResult(Activity activity, Map<String,Object> hookProps, int hookResult) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        activity.setResult(hookResult, returnIntent);
        activity.finish();
    }
    private Map<String,Object> convertHookMap(String hookString) {
        return new Gson().fromJson(hookString, new TypeToken<Map<String, Object>>() {}.getType());
    }
}
