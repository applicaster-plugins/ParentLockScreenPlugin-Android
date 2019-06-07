package com.applicaster;

public class CustomizationOptionsBundle {


    private static final CustomizationOptionsBundle instance = new CustomizationOptionsBundle();

    //private constructor to avoid client applications to use constructor
    private CustomizationOptionsBundle() {
    }

    public static CustomizationOptionsBundle getInstance() {
        return instance;
    }

    private int textColor;
    private int textSize;
    private int buttonSize;


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

    public int getButtonSize() {
        return buttonSize;
    }

    public void setButtonSize(int buttonSize) {
        this.buttonSize = buttonSize;
    }
}



