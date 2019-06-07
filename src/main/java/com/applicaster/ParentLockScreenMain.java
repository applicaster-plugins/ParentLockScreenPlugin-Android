package com.applicaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import com.applicaster.util.StringUtil;
import com.applicaster.web.plugins.iai.R;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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
    DisplayMetrics metrics;
    private Map<String, ?> hookProps;
    private HookScreenListener hookListener;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    public static int ANIMATION_DURATION = 400;
    public static int BUTTON_COUNT = 9;
    private int[] sequence = new int[3];
    private String[] numbers = new String[]{"z", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private String[] numbersEnglish = new String[]{"z", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private TextView sequenceText;
    private View mFrame;
    private TextView sequenceText_2;
    private View mBack9;
    private View mBack3;
    private TextView text_1;

    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {
        Log.e("barak", "present present present");
    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        Log.e("barak", "generateFragment present present");

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
        Log.e("barak", "executeHook");
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
//        LinkedTreeMap styles = ((LinkedTreeMap)((Map<String, ?>)hookProps.get("hook_props_screenmap")).get("styles"));

        Gson gson = new Gson();
        String yourJson = "{\n" +
                "      \"random_numbers_font_size\": \"17\",\n" +
                "      \"secondary_random_numbers_font_weight\": \"Billy-Light\",\n" +
                "      \"number_buttons_selected_background_color\": \"#ff9013fe\",\n" +
                "      \"call_for_action_text_color\": \"#ffffffff\",\n" +
                "      \"family\": \"FAMILY_1\",\n" +
                "      \"number_buttons_background_color\": \"#ffffffff\",\n" +
                "      \"force_nav_bar_hidden\": true,\n" +
                "      \"call_for_action_text_font_weight\": \"Billy\",\n" +
                "      \"secondary_random_numbers_font_size\": \"14\",\n" +
                "      \"number_color\": \"#ffffffff\",\n" +
                "      \"number_color_pressed\": \"#ff0e5daa\",\n" +
                "      \"call_for_action_text_font_size\": \"18\",\n" +
                "      \"number_font_weight\": \"Billy-Light\",\n" +
                "      \"random_numbers_color\": \"#fff8e71c\",\n" +
                "      \"random_numbers_font_weight\": \"Billy\",\n" +
                "      \"presentation\": \"push\",\n" +
                "      \"number_font_size\": \"25\",\n" +
                "      \"secondary_random_numbers_color\": \"#ffffffff\"\n" +
                "    }";
        LinkedTreeMap styles = gson.fromJson(yourJson, LinkedTreeMap.class);

        String textSize = (String) styles.get("random_numbers_font_size");//
        String secondary_random_numbers_font_weight = (String) styles.get("secondary_random_numbers_font_weight");
        String call_for_action_text_color = (String) styles.get("call_for_action_text_color");
        String family = (String) styles.get("family");
        String number_buttons_background_color = (String) styles.get("number_buttons_background_color");
        String force_nav_bar_hidden = (String) styles.get("force_nav_bar_hidden");
        String call_for_action_text_font_weight = (String) styles.get("call_for_action_text_font_weight");
        String secondary_random_numbers_font_size = (String) styles.get("secondary_random_numbers_font_size");
        String number_color = (String) styles.get("number_color");
        String number_color_pressed = (String) styles.get("number_color_pressed");
        String call_for_action_text_font_size = (String) styles.get("call_for_action_text_font_size");
        String number_font_weight = (String) styles.get("number_font_weight");
        String random_numbers_color = (String) styles.get("random_numbers_color");
        String random_numbers_font_weight = (String) styles.get("random_numbers_font_weight");
        String presentation = (String) styles.get("presentation");
        String number_font_size = (String) styles.get("number_font_size");
        String secondary_random_numbers_color = (String) styles.get("secondary_random_numbers_color");

        metrics = getContext().getResources().getDisplayMetrics();

        CustomizationOptionsBundle.getInstance().setTextColor(colorFromString(number_color));
        CustomizationOptionsBundle.getInstance().setTextSize(dpFromString(textSize));
        CustomizationOptionsBundle.getInstance().setButtonSize(dpFromString(number_font_size));

        CustomizationOptionsBundle.getInstance().setTextColor(Color.parseColor("#bdbdbd"));
        CustomizationOptionsBundle.getInstance().setTextSize(dpFromString("25"));
        CustomizationOptionsBundle.getInstance().setButtonSize(dpFromString("35"));



        return inflater.inflate(R.layout.activity_main, container, false);
    }

    private int colorFromString(String number_color) {
        return 0;
    }

    private int dpFromString(String textSize) {
        int dp = Integer.parseInt(textSize);
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);
        return pixels;
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mPinLockView.setDigitLength(BUTTON_COUNT);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        text_1 = (TextView) view.findViewById(R.id.text_1);
        sequenceText = (TextView) view.findViewById(R.id.text_2);
        sequenceText_2 = (TextView) view.findViewById(R.id.text_3);
        view.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hookListener.hookCompleted(null);
                hookListener.hookFailed((Map<String, Object>) hookProps);
            }
        });
        mFrame = view.findViewById(R.id.frame);
        mBack9 = view.findViewById(R.id.calulator_image_9);
        mBack3 = view.findViewById(R.id.calulator_image_3);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        text_1.setText(StringUtil.getTextFromKey("NumbersLockInstructionsLocalizationKey"));
//        StringUtil.getTextFromKey("Number1");

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
        sequenceText.setText("" + StringUtil.getTextFromKey("Number"+sequence[0]) + ", " + StringUtil.getTextFromKey("Number"+sequence[1]) + ", " + StringUtil.getTextFromKey("Number"+sequence[2]));
        if (!StringUtil.getTextFromKey("Number"+sequence[0]).equals(numbersEnglish[sequence[0]])){
            sequenceText_2.setText("" + numbersEnglish[sequence[0]] + ", " + numbersEnglish[sequence[1]] + ", " + numbersEnglish[sequence[2]]);
            sequenceText_2.setVisibility(View.VISIBLE);
        }

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
