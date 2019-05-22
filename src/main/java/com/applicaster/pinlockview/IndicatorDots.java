package com.applicaster.pinlockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.DimenRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.applicaster.web.plugins.iai.R;

import static com.applicaster.HookActivity.ANIMATION_DURATION;


public class IndicatorDots extends LinearLayout {

    private int mDotDiameter;
    private int mDotSpacing;
    private int mPinLength = 3;
    private int mPreviousLength;

    public IndicatorDots(Context context) {
        this(context, null);
    }

    public IndicatorDots(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorDots(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinLockView);

        try {
            mDotDiameter = (int) typedArray.getDimension(R.styleable.PinLockView_dotDiameter, getDimensionInPx(getContext(), R.dimen.default_dot_diameter));
            mDotSpacing = (int) typedArray.getDimension(R.styleable.PinLockView_dotSpacing, getDimensionInPx(getContext(), R.dimen.default_dot_spacing));

        } finally {
            typedArray.recycle();
        }

        initView(context);
    }

    private void initView(Context context) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);
        for (int i = 0; i < mPinLength; i++) {
            View dot = new View(context);
            emptyDot(dot);
            LayoutParams params = new LayoutParams(mDotDiameter,
                    mDotDiameter);
            params.setMargins(mDotSpacing, 0, mDotSpacing, 0);
            dot.setLayoutParams(params);
            addView(dot);
        }
    }


    void updateDot(int length) {
        if (length > 0) {
            if (length > mPreviousLength) {
                fillDot(getChildAt(length - 1));
            } else {
                emptyDot(getChildAt(length));
            }
            mPreviousLength = length;
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                emptyDot(v);
            }
            mPreviousLength = 0;
        }
    }

    private void emptyDot(View dot) {
        dot.setBackgroundResource(R.drawable.dot_back);
        TransitionDrawable transition = (TransitionDrawable) dot.getBackground();
        transition.resetTransition();
    }

    private void fillDot(View dot) {
        TransitionDrawable transition = (TransitionDrawable) dot.getBackground();
        transition.startTransition(ANIMATION_DURATION);
    }

    public static float getDimensionInPx(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }
}
