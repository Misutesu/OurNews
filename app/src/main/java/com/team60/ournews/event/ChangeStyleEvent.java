package com.team60.ournews.event;

/**
 * Created by wujiaquan on 2017/3/25.
 */

public class ChangeStyleEvent {

    private int[] colorPrimary;
    private int[] colorBackground;
    private int[] colorItemBackground;
    private int[] colorIcon;
    private int[] colorText;
    private int[] colorText1;
    private int[] colorText3;

    public ChangeStyleEvent(int[] colorPrimary, int[] colorBackground, int[] colorItemBackground
            , int[] colorIcon, int[] colorText, int[] colorText1, int[] colorText3) {
        this.colorPrimary = colorPrimary;
        this.colorBackground = colorBackground;
        this.colorItemBackground = colorItemBackground;
        this.colorIcon = colorIcon;
        this.colorText = colorText;
        this.colorText1 = colorText1;
        this.colorText3 = colorText3;
    }

    public int[] getColorPrimary() {
        return colorPrimary;
    }

    public int[] getColorBackground() {
        return colorBackground;
    }

    public int[] getColorItemBackground() {
        return colorItemBackground;
    }

    public int[] getColorIcon() {
        return colorIcon;
    }

    public int[] getColorText() {
        return colorText;
    }

    public int[] getColorText1() {
        return colorText1;
    }

    public int[] getColorText3() {
        return colorText3;
    }
}
