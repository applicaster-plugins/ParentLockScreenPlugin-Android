package com.applicaster;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicaster.pinlockview.IndicatorDots;
import com.applicaster.pinlockview.PinLockView;
import com.applicaster.util.StringUtil;
import com.applicaster.util.TextUtil;
import com.applicaster.web.plugins.iai.R;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Random;

public class ParentLockFragment extends Fragment implements PinLockView.PinLockListener {
    private static String TAG = ParentLockFragment.class.getSimpleName();

    DisplayMetrics metrics;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private int[] sequence = new int[3];
    private String[] numbersKeys = new String[]{"z", "NumberOne", "NumberTwo", "NumberThree", "NumberFour", "NumberFive", "NumberSix", "NumberSeven", "NumberEight", "NumberNine"};
    private String[] numbersEnglishKeys = new String[]{"z", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private TextView sequenceText;
    private TextView seconderySequenceText;
    private View mBack9;
    private View mBack3;
    private TextView upperTitle;
    private LinkedTreeMap styles;
    private LinkedTreeMap generalStyles;

    private ParentLockListener listener;

    public static String STYLES_KEY = "styles";
    public static String GENERAL_STYLE_KEY = "general styles";

    interface ParentLockListener {
        void onComplete(boolean isSuccess);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ParentLockListener)getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement ParentLockListener");
        }

    }

    public static ParentLockFragment getInstance(LinkedTreeMap styles, LinkedTreeMap generalStyles){
        ParentLockFragment fragment = new ParentLockFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(STYLES_KEY, styles);
        bundle.putSerializable(GENERAL_STYLE_KEY, generalStyles);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        styles = (LinkedTreeMap)getArguments().getSerializable(STYLES_KEY);
        generalStyles = (LinkedTreeMap)getArguments().getSerializable(GENERAL_STYLE_KEY);


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
                listener.onComplete(false);
            }
        });

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
        return TextUtil.getTypefaceFromFontKey((String)styles.get(key));
    }


    private void generateRandom() {
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            sequence[i] = r.nextInt(CustomizationOptionsBundle.getInstance().getButtonCount()) + 1;
        }
        sequenceText.setText(
                StringUtil.getTextFromKey(numbersKeys[sequence[0]]) + ", " +
                StringUtil.getTextFromKey(numbersKeys[sequence[1]]) + ", " +
                StringUtil.getTextFromKey(numbersKeys[sequence[2]]));

        if (!StringUtil.getTextFromKey(numbersKeys[sequence[0]]).equalsIgnoreCase(numbersEnglishKeys[sequence[0]])) {
            seconderySequenceText.setText("" + numbersEnglishKeys[sequence[0]] + ", " + numbersEnglishKeys[sequence[1]] + ", " + numbersEnglishKeys[sequence[2]]);
            seconderySequenceText.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPinChange(int pinLength, String pin) {
        if (pin.length() < 3) {
        } else {
            if (pin.equals("" + sequence[0] + sequence[1] + sequence[2])) {
                listener.onComplete(true);
            } else {
                mPinLockView.resetPinLockView();
                generateRandom();
            }
        }
    }

}