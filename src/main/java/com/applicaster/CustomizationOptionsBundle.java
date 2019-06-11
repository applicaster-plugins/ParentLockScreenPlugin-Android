package com.applicaster;

import android.graphics.Typeface;

public class CustomizationOptionsBundle {


    private static final CustomizationOptionsBundle instance = new CustomizationOptionsBundle();

    //private constructor to avoid client applications to use constructor
    private CustomizationOptionsBundle() {
    }

    public static CustomizationOptionsBundle getInstance() {
        return instance;
    }

    private Typeface textFont;
    private int buttonSize;
    private int textSize;
    private int textColor;
    private int textColorSelected;
    private int indicatorNormal;
    private int indicatorSelected;
    private int buttonCount;

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Typeface getTextFont() {
        return textFont;
    }

    public void setTextFont(Typeface textFont) {
        this.textFont = textFont;
    }

    public int getButtonSize() {
        return buttonSize;
    }

    public void setButtonSize(int buttonSize) {
        this.buttonSize = buttonSize;
    }



    public int getTextColorSelected() {
        return textColorSelected;
    }

    public void setTextColorSelected(int textColorSelected) {
        this.textColorSelected = textColorSelected;
    }



    public int getIndicatorNormal() {
        return indicatorNormal;
    }

    public void setIndicatorNormal(int indicatorNormal) {
        this.indicatorNormal = indicatorNormal;
    }

    public int getIndicatorSelected() {
        return indicatorSelected;
    }

    public void setIndicatorSelected(int indicatorSelected) {
        this.indicatorSelected = indicatorSelected;
    }

    public int getButtonCount() {
        return buttonCount;
    }

    public void setButtonCount(int buttonCount) {
        this.buttonCount = buttonCount;
    }
}



