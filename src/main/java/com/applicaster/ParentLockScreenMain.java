package com.applicaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.applicaster.zapproot.RootActivityManager;
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

public class ParentLockScreenMain extends Fragment implements PluginScreen, HookScreen, PinLockView.PinLockListener {
    HashMap<String, String> hookScreen = new HashMap<>();
    DisplayMetrics metrics;
    private Map<String, ?> hookProps;
    private HookScreenListener hookListener;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    public static int ANIMATION_DURATION = 400;
    private int[] sequence = new int[3];
    private String[] numbers = new String[]{"z", "NumberOne", "NumberTwo", "NumberThree", "NumberFour", "NumberFive", "NumberSix", "NumberSeven", "NumberEight", "NumberNine"};
    private String[] numbersEnglish = new String[]{"z", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private TextView sequenceText;
    private View mFrame;
    private TextView seconderySequenceText;
    private View mBack9;
    private View mBack3;
    private TextView upperTitle;
    private LinkedTreeMap styles;
    private LinkedTreeMap generalStyles;

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
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.fade_in, R.anim.slide_down);
        transaction.replace(((RootActivityManager.NavigationListener)context).getFragmentContainerID(), this);
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

        styles = getDefaultStyles();
        generalStyles = getDefaultGeneralStyles();

        if(hookScreen != null){
            String screenMapString = hookScreen.get("screenMap");
            if(screenMapString != null){
                Map screenMap = (new Gson().fromJson(screenMapString, LinkedTreeMap.class));
                if (screenMap != null){
                    styles.putAll( (LinkedTreeMap) screenMap.get("styles"));
                    generalStyles.putAll((LinkedTreeMap) screenMap.get("general"));
                }
            }
        }

        metrics = getContext().getResources().getDisplayMetrics();
        CustomizationOptionsBundle.getInstance().setButtonSize(pxFromDP_String("35"));


        String number_font_size = (String) styles.get("number_font_size");
        String number_color = (String) styles.get("number_color");
        String number_color_pressed = (String) styles.get("number_color_pressed");

        //indicator
        CustomizationOptionsBundle.getInstance().setIndicatorSelected(colorFromString((String) generalStyles.get("indicator_highlighted")));
        CustomizationOptionsBundle.getInstance().setIndicatorNormal(colorFromString((String) generalStyles.get("indicator_normal")));

        //digits
        CustomizationOptionsBundle.getInstance().setTextSize(pxFromDP_String(number_font_size));
        CustomizationOptionsBundle.getInstance().setTextFont(getFontSafely("number_font_weight"));

        CustomizationOptionsBundle.getInstance().setTextColor(colorFromString(number_color));
        CustomizationOptionsBundle.getInstance().setTextColorSelected(colorFromString(number_color_pressed));

        CustomizationOptionsBundle.getInstance().setButtonCount( Integer.parseInt((String) generalStyles.get("validation_flow_type")));


        return inflater.inflate(R.layout.activity_main, container, false);
    }

    private LinkedTreeMap getDefaultStyles(){
        LinkedTreeMap map =  new LinkedTreeMap<>();
        map.put("random_numbers_font_size", "14");
        map.put("secondary_random_numbers_font_weight", "SFUIText-LightItalic");
        map.put("number_buttons_selected_background_color", "#ff9261E2");
        map.put("call_for_action_text_color", "#9632F7");
        map.put("family", "FAMILY_1");
        map.put("number_buttons_background_color", "#ff9261E2");
        map.put("force_nav_bar_hidden", true);
        map.put("call_for_action_text_font_weight", "SFUIText-Medium");
        map.put("secondary_random_numbers_font_size", "12");
        map.put("number_color", "#9356E8");
        map.put("number_color_pressed", "#ffffff");
        map.put("call_for_action_text_font_size", "15");
        map.put("number_font_weight", "Billy-Light");
        map.put("random_numbers_color", "#C6A4FF");
        map.put("random_numbers_font_weight", "SFUIText-Regular");
        map.put("presentation", "push");
        map.put("number_font_size", "25");
        map.put("secondary_random_numbers_color", "#9F9F9F");
        return map;
    }

    private LinkedTreeMap getDefaultGeneralStyles(){
        LinkedTreeMap map =  new LinkedTreeMap();
        map.put("validation_flow_type", "3");
        map.put("background_color", "#ff0000");
        map.put("background_type", "image");
        map.put("indicator_normal", "#ffffff");
        map.put("indicator_highlighted", "#9261E2");
        map.put("background_image", "Visible");
        return map;
    }

    private int colorFromString(String number_color) {
        try{
            return Color.parseColor(number_color);
        }catch (Exception e){}
        return Color.parseColor("#eeeeee");

    }

    private int pxFromDP_String(String textSize) {
        try{
            int dp = Integer.parseInt(textSize);
            float fpixels = metrics.density * dp;
            int pixels = (int) (fpixels + 0.5f);
            return pixels;
        }catch (Exception e){}

        return  (int) (metrics.density * 15);
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        View frame = view.findViewById(R.id.frame_layout);
        View frameImage = view.findViewById(R.id.parent_lock_background);
        int buttonCount = CustomizationOptionsBundle.getInstance().getButtonCount();

        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        upperTitle = (TextView) view.findViewById(R.id.text_1);
        sequenceText = (TextView) view.findViewById(R.id.text_2);
        seconderySequenceText = (TextView) view.findViewById(R.id.text_3);
        view.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hookListener.hookCompleted(null);
//                hookListener.hookFailed((Map<String, Object>) hookProps);
                ((AppCompatActivity)getContext()).onBackPressed();
            }
        });
        mFrame = view.findViewById(R.id.frame);
        mBack9 = view.findViewById(R.id.calulator_image_9);
        mBack3 = view.findViewById(R.id.calulator_image_3);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        upperTitle.setText(StringUtil.getTextFromKey("NumbersLockInstructionsLocalizationKey"));

        if (buttonCount == 3) {
            mBack3.setVisibility(View.VISIBLE);
        } else {
            mBack9.setVisibility(View.VISIBLE);
        }
        mPinLockView.setPinLockListener(this);
        generateRandom();


        //upper Title
        upperTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxFromDP_String((String) styles.get("call_for_action_text_font_size")));
        upperTitle.setTypeface(getFontSafely("call_for_action_text_font_weight"));
        upperTitle.setTextColor(colorFromString((String) styles.get("call_for_action_text_color")));

        //first sequence
        sequenceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxFromDP_String((String) styles.get("random_numbers_font_size")));
        sequenceText.setTypeface(getFontSafely("random_numbers_font_weight"));
        sequenceText.setTextColor(colorFromString((String) styles.get("random_numbers_color")));

        //secondary sequence
        seconderySequenceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxFromDP_String((String) styles.get("secondary_random_numbers_font_size")));
        seconderySequenceText.setTypeface(getFontSafely("secondary_random_numbers_font_weight"));
        seconderySequenceText.setTextColor(colorFromString((String) styles.get("secondary_random_numbers_color")));

        //bkg
        if (((String) generalStyles.get("background_type")).equalsIgnoreCase("image")) {
            frameImage.setVisibility(View.VISIBLE);
        } else {
            frame.setBackgroundColor(colorFromString((String) generalStyles.get("background_color")));
            frameImage.setVisibility(View.GONE);
        }
    }

    private Typeface getFontSafely(String key) {
        try{
            return Typeface.createFromAsset(getContext().getAssets(),"fonts/"+(String) styles.get(key)+".otf");
        }catch (Exception e){}
        return null;
    }


    private void generateRandom() {
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            sequence[i] = r.nextInt(CustomizationOptionsBundle.getInstance().getButtonCount()) + 1;
        }
        sequenceText.setText(StringUtil.getTextFromKey(numbers[sequence[0]]) + ", " + StringUtil.getTextFromKey(numbers[sequence[1]]) + ", " + StringUtil.getTextFromKey(numbers[sequence[2]]));
        if (!StringUtil.getTextFromKey(numbers[sequence[0]]).equalsIgnoreCase(numbersEnglish[sequence[0]])) {
            seconderySequenceText.setText("" + numbersEnglish[sequence[0]] + ", " + numbersEnglish[sequence[1]] + ", " + numbersEnglish[sequence[2]]);
            seconderySequenceText.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onPinChange(int pinLength, String pin) {
        if (pin.length() < 3) {
        } else {
            if (pin.equals("" + sequence[0] + sequence[1] + sequence[2])) {
//                hookListener.hookFailed((Map<String, Object>) hookProps);
                hookListener.hookCompleted((Map<String, Object>) hookProps);
//                sendActivityResult(this, hookProps, ACTIVITY_HOOK_COMPLETED);
            } else {
                mPinLockView.resetPinLockView();
                generateRandom();
            }
        }
    }

    private void setTextColor(TextView sequenceText, int color) {
        sequenceText.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void sendActivityResult(Activity activity, Map<String, ?> hookProps, int hookResult) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(HOOK_PROPS_EXTRA, new Gson().toJson(hookProps));
        activity.setResult(hookResult, returnIntent);
        activity.finish();
    }

    private Map<String, Object> convertHookMap(String hookString) {
        return new Gson().fromJson(hookString, new TypeToken<Map<String, Object>>() {
        }.getType());
    }


}
