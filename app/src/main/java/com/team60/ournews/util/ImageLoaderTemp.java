package com.team60.ournews.util;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

/**
 * Created by Misutesu on 2017/2/21 0021.
 */

public class ImageLoaderTemp {
    public static final int CIRCLE = -1;
    public static final int CENTER_CROP = 1;
    public static final int FIT_CENTER = 2;
    public static final int FIT_XY = 3;
    public static final int CENTER_INSIDE = 4;

    public static void display(Context mContext, SimpleDraweeView simpleDraweeView, String url, int placeholder, int fitType) {
        display(mContext, simpleDraweeView, url, placeholder, 0, -1, -1, fitType);
    }

    public static void displayRes(Context mContext, SimpleDraweeView simpleDraweeView, int imgRes, int fitType) {
        display(mContext, simpleDraweeView, "", imgRes, 0, 10, imgRes, fitType);
    }

    public static void display(Context mContext, SimpleDraweeView simpleDraweeView, String url, int placeholder,
                               int round, int fitType) {
        display(mContext, simpleDraweeView, url, placeholder, round, -1, -1, fitType);
    }

    public static void display(Context mContext, SimpleDraweeView simpleDraweeView, String url, int placeholder,
                               int round, int fitType, boolean isClearCache) {
        if (isClearCache) {
            clearImgCache(url);
        }
        display(mContext, simpleDraweeView, url, placeholder, round, -1, -1, fitType);
    }

    public static void clearImgCache(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        imagePipeline.evictFromCache(uri);
    }

    public static void display(Context mContext, SimpleDraweeView simpleDraweeView, String url, int placeholder,
                               int round, int resourceType, int resId, int fitType) {
        RoundingParams roundingParams;
        if (round == -1) {
            roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);
        } else {
            roundingParams = RoundingParams.fromCornersRadius(round);
        }

        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources())
                .setFadeDuration(0)
                .setRetryImageScaleType(getScaleType(fitType))
                .setFailureImage(ContextCompat.getDrawable(mContext, placeholder))
                .setPlaceholderImage(ContextCompat.getDrawable(mContext, placeholder))
                .setFailureImageScaleType(getScaleType(fitType))
                .setPlaceholderImageScaleType(getScaleType(fitType))
                .setActualImageScaleType(getScaleType(fitType))
                // .setDesiredAspectRatio(2)
                .setRoundingParams(roundingParams)
                .build();

        simpleDraweeView.setHierarchy(hierarchy);
        if (resourceType == 10) {
            simpleDraweeView.setImageURI(Uri.parse("res:// /" + resId));
        } else {
            simpleDraweeView.setImageURI(Uri.parse(url));
        }
    }

    private static ScalingUtils.ScaleType getScaleType(int scaleType) {
        switch (scaleType) {
            case 1:
                return ScalingUtils.ScaleType.CENTER_CROP;
            case 2:
                return ScalingUtils.ScaleType.FIT_CENTER;
            case 3:
                return ScalingUtils.ScaleType.FIT_XY;
            case 4:
                return ScalingUtils.ScaleType.CENTER_INSIDE;
        }
        return ScalingUtils.ScaleType.CENTER_CROP;
    }
}
