package com.team60.ournews.event;

/**
 * Created by Misutesu on 2017/4/21 0021.
 */

public class UpdateProgressEvent {
    private int progress;

    public UpdateProgressEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }
}
