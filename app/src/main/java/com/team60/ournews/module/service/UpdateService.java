package com.team60.ournews.module.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.team60.ournews.R;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.OkHttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.team60.ournews.util.RetrofitUtil.BASE_URL;

public class UpdateService extends Service {

    private final int NOTIFY_ID = 101;

    private NotificationCompat.Builder mBuilder;

    private InputStream is;
    private FileOutputStream fos;

    private boolean isDestroy;

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            download(name);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        stopForeground(true);
        super.onDestroy();
    }

    private void download(final String name) {
        mBuilder = new NotificationCompat.Builder(UpdateService.this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.is_download_new_version) + " 0%")
                .setSmallIcon(R.drawable.min_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setProgress(100, 0, true)
                .setAutoCancel(false);
        startForeground(NOTIFY_ID, mBuilder.build());
        Request request = new Request.Builder().header("Accept-Encoding", "identity")
                .url(BASE_URL + "downloadApk?name=" + name).build();
        OkHttpClient client = OkHttpUtil.getOkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                downloadError();
                stopForeground(true);
                stopSelf();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    File file = getCacheDir();
                    file = new File(file, "NewVersion");
                    if (!file.exists()) file.mkdirs();
                    file = new File(file, "OurNews.apk");
                    if (file.exists()) file.delete();

                    byte[] buf = new byte[2048];
                    int len;
                    long sum = 0;
                    int oldProgress = 0;
                    is = response.body().byteStream();
                    long allLength = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    Log.d("TAG", "allLength : " + allLength);
                    while ((len = is.read(buf)) != -1) {
                        if (!isDestroy) {
                            fos.write(buf, 0, len);
                            sum += len;
                            Log.d("TAG", "sum : " + sum);
                            int progress = (int) (((float) sum) / allLength * 100);
                            if (oldProgress != progress) {
                                oldProgress = progress;
                                Log.d("TAG", "progress : " + progress);
//                                mBuilder.setProgress(100, progress, true)
//                                        .setContentText(getString(R.string.is_download_new_version) + " " + progress + "%");
//                                startForeground(NOTIFY_ID, mBuilder.build());
                            }
                        } else {
                            return;
                        }
                    }
                    MyUtil.installAPK(UpdateService.this, file);
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadError();
                } finally {
                    if (fos != null) {
                        try {
                            fos.flush();
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
            }
        });
    }

    private void downloadError() {
        File file = getCacheDir();
        file = new File(file, "NewVersion" + File.separator + "OurNews.apk");
        if (file.exists()) file.delete();
    }
}
