package com.team60.ournews.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.team60.ournews.module.model.New;
import com.team60.ournews.module.ui.activity.NewActivity;
import com.team60.ournews.module.ui.activity.PhotoActivity;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class SkipUtil {

    public static final String SHARE_IMAGE_VIEW = "shareImageView";

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.startActivity(activity, intent
                    , ActivityOptions.makeSceneTransitionAnimation(activity, view, SHARE_IMAGE_VIEW).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static void startNewActivity(Activity activity, New n, View view) {
        Intent intent = new Intent(activity, NewActivity.class);
        intent.putExtra(New.class.getName(), n);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.startActivity(activity, intent
                    , ActivityOptions.makeSceneTransitionAnimation(activity, view, SHARE_IMAGE_VIEW).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
