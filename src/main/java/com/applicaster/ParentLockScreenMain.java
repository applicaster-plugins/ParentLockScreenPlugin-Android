package com.applicaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicaster.hook_screen.HookScreen;
import com.applicaster.hook_screen.HookScreenListener;
import com.applicaster.pinlockview.IndicatorDots;
import com.applicaster.pinlockview.PinLockView;
import com.applicaster.plugin_manager.screen.PluginScreen;
import com.applicaster.web.plugins.iai.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.applicaster.hook_screen.HookScreenManager.ACTIVITY_HOOK_RESULT_CODE;
import static com.applicaster.hook_screen.HookScreenManager.HOOK_PROPS_EXTRA;

public class ParentLockScreenMain  extends Fragment implements PluginScreen, HookScreen,PinLockView.PinLockListener {
    HashMap<String, String> hookScreen = new HashMap<>();
    private Map<String, ?> hookProps;
    private HookScreenListener hookListener;
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

    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {
        Log.e("bn", "present present present");
    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        Log.e("bn", "generateFragment present present");

        return null;
    }

    //Hook Screen Interface implementation
    @NotNull
    @Override
    public HashMap<String, String> getHook() {
        return this.hookScreen;
    }

    @Override
    public void setHook(@NotNull HashMap<String, String> hookScreen) {
        this.hookScreen = hookScreen;
    }

    //execute hook
    @Override
    public void executeHook(@NotNull Context context, @NotNull HookScreenListener hookScreenListener, @Nullable Map<String, ?> map) {
        hookProps = map;

        hookListener = hookScreenListener;
//        hookListener.hookCompleted((Map<String, Object>) hookProps);
//
//        Intent intent = new Intent(context, this.getClass());
//        startActivityForResult(intent, context, map);
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim. exit_to_left, R.anim.enter_from_right,R.anim.slide_out_right, R.anim.slide_in_right);
        transaction.replace(R.id.content_frame, this);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startActivityForResult(Intent intent, Context context, Map<String, ?> hookProps) {
        if (hookProps != null) {
            intent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        }
        ((Activity) context).startActivityForResult(intent, ACTIVITY_HOOK_RESULT_CODE);
    }

    //Android specific method to return the hookListener from Screen Hook
    @NotNull
    @Override
    public HookScreenListener getListener() {
        return hookListener;
    }

    //Specifies the logic for cases when user dismissed the hook.
    @Override
    public void hookDismissed() {

    }

    //Determines if failed hook will abort.
    @Override
    public boolean isFlowBlocker() {
        return true;
    }

    //Determines if hook can me presented every time specific screen loads.
    @Override
    public boolean isRecurringHook() {
        return true;
    }

    //Determines if hook screen will be presenting to UI.
    @Override
    public boolean shouldPresent() {
        return true;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mPinLockView.setDigitLength(BUTTON_COUNT);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        sequenceText = (TextView) view.findViewById(R.id.text_2);
        sequenceText_2 = (TextView) view.findViewById(R.id.text_3);
        view.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hookListener.hookCompleted(null);
                hookListener.hookFailed(null);
            }
        });
        mFrame = view.findViewById(R.id.frame);
        mBack9 = view.findViewById(R.id.calulator_image_9);
        mBack3 = view.findViewById(R.id.calulator_image_3);
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
            setTextColor(sequenceText, com.applicaster.web.plugins.iai.R.color.perp_text_2);
        } else {
            if (pin.equals("" + sequence[0] + sequence[1] + sequence[2])) {
//                hookListener.hookFailed((Map<String, Object>) hookProps);
                hookListener.hookCompleted((Map<String, Object>) hookProps);
//                sendActivityResult(this, hookProps, ACTIVITY_HOOK_COMPLETED);
            } else {
                setTextColor(sequenceText, R.color.red);
                mPinLockView.resetPinLockView();
                generateRandom();
            }
        }
    }

    private void setTextColor(TextView sequenceText, int color) {
        sequenceText.setTextColor(ContextCompat.getColor(getContext(), color));
    }
    private void sendActivityResult(Activity activity, Map<String,?> hookProps, int hookResult) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        activity.setResult(hookResult, returnIntent);
        activity.finish();
    }
    private Map<String,Object> convertHookMap(String hookString) {
        return new Gson().fromJson(hookString, new TypeToken<Map<String, Object>>() {}.getType());
    }


}
