package com.team60.ournews.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class DateUtil {

    public static String getNowTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }
}
