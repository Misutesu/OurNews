package com.team60.ournews.module.model;

/**
 * Created by Misutesu on 2016/10/19 0019.
 */

public class Theme {
    private int color;
    private int themeId;

    public Theme(int color, int themeId) {
        this.color = color;
        this.themeId = themeId;
    }

    public int getColor() {
        return color;
    }

    public int getThemeId() {
        return themeId;
    }
}
