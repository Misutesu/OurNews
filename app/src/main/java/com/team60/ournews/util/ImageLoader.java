package com.team60.ournews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */

public class ImageLoader {

    public static ImageLoader.Builder init(Context context, int imgId) {
        return new ImageLoader.Builder(context, imgId);
    }

    public static ImageLoader.Builder init(Context context, String url) {
        return new ImageLoader.Builder(context, url);
    }

    public static ImageLoader.Builder init(Context context, File file) {
        return new ImageLoader.Builder(context, file);
    }

    public static class Builder {

        private DrawableTypeRequest mDrawableTypeRequest;

        public Builder(Context context, int imgId) {
            mDrawableTypeRequest = Glide.with(context).load(imgId);
        }

        public Builder(Context context, String url) {
            mDrawableTypeRequest = Glide.with(context).load(url);
        }

        public Builder(Context context, File file) {
            mDrawableTypeRequest = Glide.with(context).load(file);
        }

        public void bitmapTransform(Transformation<Bitmap>... bitmapTransformations) {
            mDrawableTypeRequest.bitmapTransform(bitmapTransformations);
        }

        public void skipMemoryCache() {
            mDrawableTypeRequest.skipMemoryCache(true);
        }

        public void diskCacheMode(DiskCacheStrategy strategy) {
            mDrawableTypeRequest.diskCacheStrategy(strategy);
        }

        public void override(int width, int height) {
            mDrawableTypeRequest.override(width, height);
        }

        public void intoImage(ImageView img) {
            mDrawableTypeRequest.into(img);
        }
    }

    public static DrawableTypeRequest<Integer> loadImage(Context context, int imgId) {
        return Glide.with(context).load(imgId);
    }

    public static DrawableTypeRequest<File> loadImage(Context context, File file) {
        return Glide.with(context).load(file);
    }

    public static DrawableTypeRequest<String> loadImage(Context context, String url) {
        return Glide.with(context).load(url);
    }

    public static void intoImageView(ImageView img) {

    }
}
