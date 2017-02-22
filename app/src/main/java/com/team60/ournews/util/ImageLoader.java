package com.team60.ournews.util;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;

/**
 * Created by Misutesu on 2017/2/22.
 */

public class ImageLoader {

    public static final int CENTER_CROP = 1;
    public static final int FIT_CENTER = 2;
    public static final int FIT_XY = 3;
    public static final int CENTER_INSIDE = 4;

    public static final int CIRCLE = -1;

    private ImageLoader() {
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder {

        private int durationTime = 300;
        private int scaleType = CENTER_CROP;
        private int failureImgResId = -1;
        private int placeImgResId = -1;
        private int circleRound = 0;
        private int borderWidth = 0;
        private int borderColor = 0;

        private Uri mUri;

        private Context mContext;

        private Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setDurationTime(int time) {
            if (time > 0)
                this.durationTime = time;
            return this;
        }

        public Builder setScaleType(int scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Builder setFailureImage(int res) {
            failureImgResId = res;
            return this;
        }

        public Builder setPlaceImage(int res) {
            placeImgResId = res;
            return this;
        }

        public Builder setCircleRound(int round) {
            circleRound = round;
            return this;
        }

        public Builder setCircle() {
            circleRound = -1;
            return this;
        }

        public Builder setBorder(int width, int color) {
            if (width > 0) {
                borderWidth = width;
                borderColor = color;
            }
            return this;
        }

        public Builder setImage(String url) {
            if (!TextUtils.isEmpty(url)) {
                mUri = getUri(url);
            }
            return this;
        }

        public Builder setImage(File file) {
            if (file != null && file.exists()) {
                mUri = getUri("file://" + file.getAbsolutePath());
            }
            return this;
        }

        public Builder setImage(int resId) {
            mUri = getUri("res:// /" + resId);
            return this;
        }

        public Builder clearImgCache() {
            ImageLoader.clearImgCache(mUri);
            return this;
        }

        public void into(SimpleDraweeView simpleDraweeView) {
            if (mUri != null && simpleDraweeView != null) {

                GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
                hierarchy.setFadeDuration(durationTime);
                ScalingUtils.ScaleType scale = getScaleType(scaleType);
                if (failureImgResId != -1) {
                    hierarchy.setFailureImage(ContextCompat.getDrawable(mContext, failureImgResId), scale);
                }
                if (placeImgResId != -1) {
                    hierarchy.setPlaceholderImage(ContextCompat.getDrawable(mContext, placeImgResId), scale);
                }
                hierarchy.setActualImageScaleType(scale);

                RoundingParams roundingParams = null;
                if (circleRound == -1) {
                    roundingParams = new RoundingParams();
                    roundingParams.setRoundAsCircle(true);
                } else if (circleRound != 0) {
                    roundingParams = RoundingParams.fromCornersRadius(circleRound);
                }
                if (borderWidth > 0) {
                    if (roundingParams == null) {
                        roundingParams = new RoundingParams();
                    }
                    roundingParams.setBorder(borderColor, borderWidth);
                }

                if (roundingParams != null) {
                    hierarchy.setRoundingParams(roundingParams);
                }

                simpleDraweeView.setHierarchy(hierarchy);
                simpleDraweeView.setImageURI(mUri);
            }
        }
    }

    private static ScalingUtils.ScaleType getScaleType(int scaleType) {
        switch (scaleType) {
            case 2:
                return ScalingUtils.ScaleType.FIT_CENTER;
            case 3:
                return ScalingUtils.ScaleType.FIT_XY;
            case 4:
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            default:
                return ScalingUtils.ScaleType.CENTER_CROP;
        }
    }

    public static void clearImgChche(File file) {
        if (file != null && file.exists())
            clearImgCache(getUri("file://" + file.getAbsolutePath()));
    }

    public static void clearImgChche(String url) {
        if (!TextUtils.isEmpty(url))
            clearImgCache(getUri(url));
    }

    private static void clearImgCache(Uri uri) {
        if (uri != null) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(uri);
            imagePipeline.evictFromDiskCache(uri);
            imagePipeline.evictFromCache(uri);
        }
    }

    private static Uri getUri(String uri) {
        return Uri.parse(uri);
    }
}
