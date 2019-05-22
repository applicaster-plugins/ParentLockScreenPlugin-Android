package com.applicaster.pinlockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.applicaster.web.plugins.iai.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.applicaster.HookActivity.BUTTON_COUNT;


public class PinLockView extends RecyclerView {

    private int mDigitLength = 9;
    private String mPin = "";
    private int mHorizontalSpacing, mVerticalSpacing;
    private int mTextColor;
    private int mTextSize, mButtonSize;

    private IndicatorDots mIndicatorDots;
    private PinLockAdapter mAdapter;
    private PinLockListener mPinLockListener;
    private CustomizationOptionsBundle mCustomizationOptionsBundle;

    private PinLockAdapter.OnNumberClickListener mOnNumberClickListener
            = new PinLockAdapter.OnNumberClickListener() {
        @Override
        public void onNumberClicked(int keyValue) {
            if (mPin.length() < 3) {
                mPin = mPin.concat(String.valueOf(keyValue));
                if (isIndicatorDotsAttached()) {
                    mIndicatorDots.updateDot(mPin.length());
                }
                if (mPin.length() == 1) {
                    mAdapter.setPinLength(mPin.length());
                }
                if (mPinLockListener != null) {
                        mPinLockListener.onPinChange(mPin.length(), mPin);
                }
            } else {
                resetPinLockView();
                mPin = mPin.concat(String.valueOf(keyValue));

                if (isIndicatorDotsAttached()) {
                    mIndicatorDots.updateDot(mPin.length());
                }

                if (mPinLockListener != null) {
                    mPinLockListener.onPinChange(mPin.length(), mPin);
                }
            }
        }
    };


    public PinLockView(Context context) {
        super(context);
        init(null, 0);
    }

    public PinLockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PinLockView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attributeSet, int defStyle) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.PinLockView);

        try {
            mHorizontalSpacing = (int) typedArray.getDimension(R.styleable.PinLockView_keypadHorizontalSpacing, getDimensionInPx(getContext(), R.dimen.default_horizontal_spacing));
            mVerticalSpacing = (int) typedArray.getDimension(R.styleable.PinLockView_keypadVerticalSpacing, getDimensionInPx(getContext(), R.dimen.default_vertical_spacing));
            mTextColor = typedArray.getColor(R.styleable.PinLockView_keypadTextColor, ContextCompat.getColor(getContext(), R.color.perp));
            mTextSize = (int) typedArray.getDimension(R.styleable.PinLockView_keypadTextSize, getDimensionInPx(getContext(), R.dimen.default_text_size));
            mButtonSize = (int) typedArray.getDimension(R.styleable.PinLockView_keypadButtonSize, getDimensionInPx(getContext(), R.dimen.default_button_size));
        } finally {
            typedArray.recycle();
        }

        mCustomizationOptionsBundle = new CustomizationOptionsBundle();
        mCustomizationOptionsBundle.setTextColor(mTextColor);
        mCustomizationOptionsBundle.setTextSize(mTextSize);
        mCustomizationOptionsBundle.setButtonSize(mButtonSize);

        initView();
    }

    private void initView() {
        setLayoutManager(new GridLayoutManager(getContext(), 3));

        mAdapter = new PinLockAdapter(getContext());
        mAdapter.setKeyValues(keySet(BUTTON_COUNT));
        mAdapter.setOnItemClickListener(mOnNumberClickListener);
        mAdapter.setCustomizationOptions(mCustomizationOptionsBundle);
        setAdapter(mAdapter);

        addItemDecoration(new ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private ArrayList<Integer> keySet(int length) {
        if (length==9){
            ArrayList<Integer> s = new ArrayList<Integer>();
            Collections.addAll(s, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            return s;
        }
        ArrayList<Integer> s = new ArrayList<Integer>();
        Collections.addAll(s, 1, 2, 3);
        return s;
    }


    public void setPinLockListener(PinLockListener pinLockListener) {
        this.mPinLockListener = pinLockListener;
    }



    private void clearInternalPin() {
        mPin = "";
    }


    public void resetPinLockView() {

        clearInternalPin();

        mAdapter.setPinLength(mPin.length());
        mAdapter.setKeyValues(keySet(mDigitLength));

        if (mIndicatorDots != null) {
            mIndicatorDots.updateDot(mPin.length());
        }
    }

    public boolean isIndicatorDotsAttached() {
        return mIndicatorDots != null;
    }


    public void attachIndicatorDots(IndicatorDots mIndicatorDots) {
        this.mIndicatorDots = mIndicatorDots;
    }

    public void setDigitLength(int length) {
        mDigitLength =length;
    }

    public interface PinLockListener {
        void onPinChange(int pinLength, String intermediatePin);
    }
    public static float getDimensionInPx(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }
}
