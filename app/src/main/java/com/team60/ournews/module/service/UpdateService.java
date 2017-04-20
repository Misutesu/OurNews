package com.team60.ournews.module.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.team60.ournews.R;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.OkHttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateService extends Service {

    private final int NOTIFY_CODE = 101;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;

    private InputStream is;
    private FileOutputStream fos;

    private Disposable disposable;

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            download(url);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (disposable != null) disposable.dispose();
        stopForeground(true);
        super.onDestroy();
    }

    private void download(final String url) {
        disposable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Request request = new Request.Builder().url(url).build();
                OkHttpClient client = OkHttpUtil.getOkHttpClient();
                Response response = client.newCall(request).execute();
                File file = getCacheDir();
                file = new File(file, "NewVersion");
                if (!file.exists()) file.mkdirs();

                long allLength = response.body().contentLength();
                file = new File(file, "OurNews.apk");
                if (file.exists()) file.delete();

                byte[] buf = new byte[1024];
                int len;
                long sum = 0;
                int oldProgress = 0;
                is = response.body().byteStream();
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / allLength * 100);
                    if (oldProgress != progress && mBuilder != null) {
                        oldProgress = progress;
                        emitter.onNext(progress);
                    }
                }
                MyUtil.installAPK(UpdateService.this, file);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    protected void onStart() {
                        mBuilder = new NotificationCompat.Builder(UpdateService.this)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText(getString(R.string.is_download_new_version) + " 0%")
                                .setSmallIcon(R.drawable.min_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setWhen(System.currentTimeMillis())
                                .setProgress(100, 0, true)
                                .setAutoCancel(false);
                        startForeground(NOTIFY_CODE, mBuilder.build());
                        request(1);
                    }

                    @Override
                    public void onNext(Integer progress) {
                        request(100);
                        mBuilder.setProgress(100, progress, true)
                                .setContentText(getString(R.string.is_download_new_version) + " " + progress + "%");
                        mManager.notify(NOTIFY_CODE, mBuilder.build());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        File file = getCacheDir();
                        file = new File(file, "NewVersion" + File.separator + "OurNews.apk");
                        if (file.exists()) file.delete();
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        stopForeground(true);
                        stopSelf();
                    }
                });
    }
}
