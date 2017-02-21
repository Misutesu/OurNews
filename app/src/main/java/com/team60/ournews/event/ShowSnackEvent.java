package com.team60.ournews.event;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class ShowSnackEvent {
    private String message;

    public ShowSnackEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
