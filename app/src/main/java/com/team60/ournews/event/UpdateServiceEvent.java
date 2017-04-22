package com.team60.ournews.event;

/**
 * Created by Misutesu on 2017/4/21 0021.
 */

public class UpdateServiceEvent {

    public static final int START = 0;
    public static final int HIDE = 1;
    public static final int CANCEL = 2;

    private int type;

    public UpdateServiceEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
