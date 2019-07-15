package com.applicaster.pinlockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.DimenRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applicaster.CustomizationOptionsBundle;
import com.applicaster.web.plugins.iai.R;

import static com.applicaster.Constants.ANIMATION_DURATION;


public class IndicatorDots extends LinearLayout {
    Drawable[] layers = new Drawable[2];
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
        int strokeWidth = 1;
        int fillColor = CustomizationOptionsBundle.getInstance().getIndicatorNormal();
        int strokeColor = CustomizationOptionsBundle.getInstance().getIndicatorSelected();
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(fillColor);
        gD.setShape(GradientDrawable.OVAL);
        gD.setStroke(strokeWidth, strokeColor);
        layers[0] = gD;
        int color = CustomizationOptionsBundle.getInstance().getIndicatorSelected();
        GradientDrawable gD2 = new GradientDrawable();
        gD2.setColor(color);
        gD2.setShape(GradientDrawable.OVAL);
        layers[1] = gD2;
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
            ImageView dot = new ImageView(context);
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
                fillDot((ImageView) getChildAt(length - 1));
            } else {
                emptyDot((ImageView) getChildAt(length));
            }
            mPreviousLength = length;
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                ImageView v = (ImageView) getChildAt(i);
                emptyDot(v);
            }
            mPreviousLength = 0;
        }
    }

    private void emptyDot(ImageView dot) {
        TransitionDrawable transition = new TransitionDrawable(layers);
        dot.setImageDrawable(transition);
        transition.resetTransition();
    }

    private void fillDot(ImageView dot) {
        TransitionDrawable transition = (TransitionDrawable) dot.getDrawable();
        transition.startTransition(ANIMATION_DURATION);


    }

    public static float getDimensionInPx(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }
}
