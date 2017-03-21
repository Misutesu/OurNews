package com.team60.ournews.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.common.Constants;
import com.team60.ournews.listener.DownListener;
import com.team60.ournews.module.connection.RetrofitUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

public class MyUtil {

    public static void sendLog(Object object, String message) {
        if (Constants.IS_DEBUG_MODE)
            Log.d(object.getClass().getName(), message);
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static boolean isLoginName(String loginName) {
        return loginName != null && loginName.length() > 5 && loginName.length() < 13;
    }

    public static boolean isPassword(String password) {
        return password != null&&password.length() > 5 && password.length() < 13;
    }

    public static void openKeyBord(Context context, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void closeKeyBord(Context context, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static String getPhotoUrl(@NonNull String photoName) {
        return RetrofitUtil.BASE_URL + "downloadImage?name=" + photoName;
    }

    public static Disposable savePhoto(final Context context, final String photoUrl, @NonNull final DownListener downListener) {
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> flowable) throws Exception {
                File file = FrescoLoader.getLocalCache(context, FrescoLoader.getUri(photoUrl));
                if (file != null && file.exists()) {
                    File saveFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
                    String fileName = "OurNews";
                    File appDir = new File(saveFile, fileName);
                    if (!appDir.exists()) {
                        if (appDir.mkdirs()) {
                            callSystemUpdate(context, copyFileToOtherFolder(file, saveFile, System.currentTimeMillis() + ".png"));
                            flowable.onNext(1);
                        } else {
                            flowable.onError(new IOException());
                        }
                    } else {
                        callSystemUpdate(context, copyFileToOtherFolder(file, saveFile, System.currentTimeMillis() + ".png"));
                        flowable.onNext(1);
                    }
                } else {
                    flowable.onError(new FileNotFoundException());
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        downListener.success();
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        downListener.error();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static File copyFileToOtherFolder(@NonNull File file, @NonNull File saveFolder
            , @NonNull String fileName) throws IOException {
        if (!file.exists() || !saveFolder.exists() || !saveFolder.isDirectory())
            return null;
        File currentFile = new File(saveFolder, fileName);
        int byteRead;
        InputStream inputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
        byte[] buffer = new byte[1024];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, byteRead);
        }
        inputStream.close();
        fileOutputStream.close();
        return currentFile;
    }

    public static void callSystemUpdate(Context context, File file) throws FileNotFoundException {
        if (file == null || !file.exists())
            throw new FileNotFoundException();
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                file.getAbsolutePath(), file.getName(), null);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }
}
