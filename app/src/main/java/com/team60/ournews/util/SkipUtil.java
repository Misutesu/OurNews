package com.team60.ournews.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.ui.activity.NewActivity;
import com.team60.ournews.module.ui.activity.PhotoActivity;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class SkipUtil {

    public final static String VIEW_INFO = "view_info";
    public final static String VIEW_X = "view_x";
    public final static String VIEW_Y = "view_y";
    public final static String VIEW_WIDTH = "view_width";
    public final static String VIEW_HEIGHT = "view_height";

    public static void startPhotoActivity(Activity activity, String title, String photoName) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        if (title != null)
            intent.putExtra(PhotoActivity.TITLE_VALUE, title);
        intent.putExtra(PhotoActivity.PHOTO_NAME_VALUE, photoName);

        activity.startActivity(intent);
    }

    public static void startPhotoActivity(Activity activity, String title, String photoName, View view) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        if (title != null)
            intent.putExtra(PhotoActivity.TITLE_VALUE, title);
        intent.putExtra(PhotoActivity.PHOTO_NAME_VALUE, photoName);

        activity.startActivity(intent);
    }

    public static void startNewActivity(Activity activity, New n, View view) {
        Intent intent = new Intent(activity, NewActivity.class);
        intent.putExtra(New.class.getName(), n);
        intent.putExtra(VIEW_INFO, captureValues(view));
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static Bundle captureValues(@NonNull View view) {
        Bundle b = new Bundle();
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        b.putInt(VIEW_X, screenLocation[0]);
        b.putInt(VIEW_Y, screenLocation[1]);
        b.putInt(VIEW_WIDTH, view.getWidth());
        b.putInt(VIEW_HEIGHT, view.getHeight());
        return b;
    }
}
