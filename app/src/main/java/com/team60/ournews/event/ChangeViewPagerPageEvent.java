package com.team60.ournews.event;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class ChangeViewPagerPageEvent {
    private int page;

    public ChangeViewPagerPageEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
